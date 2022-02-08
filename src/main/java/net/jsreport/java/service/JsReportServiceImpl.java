package net.jsreport.java.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.jsreport.java.JsReportException;
import net.jsreport.java.dto.*;
import net.jsreport.java.rest.GetTemplateResponse;
import net.jsreport.java.rest.ListTemplateResponse;
import net.jsreport.java.rest.RenderRequest;
import okhttp3.*;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class JsReportServiceImpl implements JsReportService {

    public static final String HEADER_FILE_EXTENSION = "File-Extension";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_AUTHORIZATION = "Authorization";

    private JsReportRetrofitService jsreportRetrofitService;

    private JsReportServiceImpl(String serverBaseUrl, Interceptor authIntercetor, ServiceTimeout serviceTimeout) {
        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient().newBuilder();

        if (authIntercetor != null) {
            okHttpClientBuilder.addInterceptor(authIntercetor);
        }

        if (serviceTimeout != null) {
            okHttpClientBuilder
                    .callTimeout(serviceTimeout.getCallTimeout())
                    .connectTimeout(serviceTimeout.getConnectTimeout())
                    .readTimeout(serviceTimeout.getReadTimeout())
                    .writeTimeout(serviceTimeout.getWriteTimeout());
        }

        Retrofit retrofit =
                new Retrofit.Builder()
                        .baseUrl(serverBaseUrl)
                        .client(okHttpClientBuilder.build())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

        jsreportRetrofitService = retrofit.create(JsReportRetrofitService.class);
    }

    public JsReportServiceImpl(String baseServerUrl, String username, String password, ServiceTimeout serviceTimeout) {
        this(
                baseServerUrl,
                new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        Request.Builder builder =
                                originalRequest
                                        .newBuilder()
                                        .header(
                                                HEADER_AUTHORIZATION,
                                                Credentials.basic(username, password)
                                        );

                        Request newRequest = builder.build();
                        return chain.proceed(newRequest);
                    }
                },
                serviceTimeout
        );
    }

    public JsReportServiceImpl(String baseServerUrl, String username, String password) {
        this(
                baseServerUrl,
                new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        Request.Builder builder =
                                originalRequest
                                        .newBuilder()
                                        .header(
                                                HEADER_AUTHORIZATION,
                                                Credentials.basic(username, password)
                                        );

                        Request newRequest = builder.build();
                        return chain.proceed(newRequest);
                    }
                },
                null
        );
    }

    public JsReportServiceImpl(String baseServerUrl) {
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseServerUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        jsreportRetrofitService = retrofit.create(JsReportRetrofitService.class);
    }

    public JsReportServiceImpl(String baseServerUrl, ServiceTimeout serviceTimeout) {
        this(baseServerUrl, null, serviceTimeout);
    }

    @Override
    public Report render(RenderRequest renderRequest) throws JsReportException {
        Call<ResponseBody> callRender = jsreportRetrofitService.render(renderRequest);
        return processResponse(callRender);
    }

    @Override
    public Report render(Map<String, Object> renderRequest) throws JsReportException {
        Call<ResponseBody> callRender = jsreportRetrofitService.render(renderRequest);
        return processResponse(callRender);
    }

    @Override
    public Report render(String templateName, Object data, Options options) throws JsReportException {
        return render(new RenderRequest(new Template(templateName), data, options));
    }

    @Override
    public Report render(String templateName, Object data) throws JsReportException {
        return render(new RenderRequest(new Template(templateName), data));
    }

    @Override
    public Template putTemplate(Template template) throws JsReportException {
        return JsReportServiceImpl.<Template>processSyncCall(jsreportRetrofitService.putTemplate(template)).body();
    }

    @Override
    public Template getTemplate(String id) throws JsReportException {
        return JsReportServiceImpl.<GetTemplateResponse>processSyncCall(jsreportRetrofitService.getTemplate(id)).body().getTemplate();
    }

    @Override
    public List<Template> listTemplates() throws JsReportException {
        return JsReportServiceImpl.<ListTemplateResponse>processSyncCall(jsreportRetrofitService.listTemplates()).body().getValue();
    }

    @Override
    public void removeTemplate(String id) throws JsReportException {
        processSyncCall(jsreportRetrofitService.removeTemplate(id));
    }

    @Override
    public Future<Report> renderAsync(RenderRequest renderTemplateRequest) {
        CompletableFuture<Report> future = new CompletableFuture<>();

        Call<ResponseBody> call = jsreportRetrofitService.render(renderTemplateRequest);

        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Report report = new Report();
                        report.setContent(response.body().byteStream());
                        report.setContentType(response.headers().get(HEADER_CONTENT_TYPE));
                        report.setFileExtension(response.headers().get(HEADER_FILE_EXTENSION));
                        future.complete(report);
                    } else {
                        future.completeExceptionally(new JsReportException("Invalid body response from server! Returns empty body!"));

                    }
                } else {
                    future.completeExceptionally(new JsReportException(String.format("Invalid status code (%d) !!!", response.code())));

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                future.completeExceptionally(new JsReportException(t));

            }
        });

        return future;
    }

    private Report processResponse(Call<ResponseBody> callRender) throws JsReportException {
        try {
            Response<ResponseBody> syncResponse = callRender.execute();
            throwJsReportOnError(syncResponse);

            Report report = new Report();

            if (syncResponse.body() != null) {
                report.setContent(syncResponse.body().byteStream());
                report.setContentType(syncResponse.headers().get(HEADER_CONTENT_TYPE));
                report.setFileExtension(syncResponse.headers().get(HEADER_FILE_EXTENSION));

            } else {
                throw new JsReportException("Invalid body response from server! Returns empty body!");

            }

            return report;
        } catch (IOException e) {
            throw new JsReportException(e);
        }
    }

    private static <R> Response<R> processSyncCall(Call<?> call) throws JsReportException {
        try {
            Response<?> response = call.execute();
            throwJsReportOnError(response);

            return (Response<R>) response;
        } catch (IOException e) {
            throw new JsReportException(e);
        }
    }

    private static void throwJsReportOnError(Response<?> response) throws JsReportException {
        if (! response.isSuccessful()) {
            throw new JsReportException(String.format("Invalid status code (%d) !!!", response.code()));
        }
    }
}
