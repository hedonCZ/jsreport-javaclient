package net.jsreport.java;

import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

// TODO - may use factory pattern ???
public class ReportingServiceImpl implements ReportingService {

    public static final String HEADER_FILE_EXTENSION = "File-Extension";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String MIME_APPLICATION_JSON = "application/json";

    private RegistryBuilder<ConnectionSocketFactory> registryBuilder;

    private String username;
    private String password;

    private URI uri;

    private Gson gson = new Gson();

    public ReportingServiceImpl() {
        registryBuilder = RegistryBuilder.create();
    }

    /**
     * @see {@link net.jsreport.java.ReportingService#renderAsync(String, Object)}
     */
    public Future<Report> renderAsync(final String templateShortid, final Object data) throws JsReportException {
        FutureTask<Report> reportFutureTask =
                new FutureTask<Report>(new Callable<Report>() {
                    public Report call() throws Exception {
                    try {
                        return render(templateShortid, data);
                    } catch (JsReportException e) {
                        e.printStackTrace();
                    }

                    return null;
                    }
                });
//        reportFutureTask.run();
        return reportFutureTask;
    }

    /**
     * @see {@link net.jsreport.java.ReportingService#render(String, Object)}
     */
    public Report render(String templateShortid, Object data) throws JsReportException {
        Template template = new Template();
        template.setShortid(templateShortid);

        RenderRequest renderRequest = new RenderRequest();
        renderRequest.setTemplate(template);
        renderRequest.setData(data);

        return render(renderRequest);
    }

    /**  */
    public Future<Report> renderAsync(String templateShortid, String jsonData) throws JsReportException {
        return null;
    }


    /**
     * @see {@link net.jsreport.java.ReportingService#render(String, String)}
     */
    public Report render(String templateShortid, String jsonData) throws JsReportException {
        String requestString = String.format("{ \"template\" : { \"shortid\" : \"%s\"}, \"data\" : %s }", templateShortid, jsonData);

        try {
            return renderString(requestString);
        } catch (IOException e) {
            throw new JsReportException(e);
        }
    }
    /** @see {@link net.jsreport.java.ReportingService#renderAsync(RenderRequest)} */
    public Future<Report> renderAsync(RenderRequest request) {
        return null;
    }

    /** @see {@link net.jsreport.java.ReportingService#render(RenderRequest)} */
    public Report render(RenderRequest request) throws JsReportException {


        try {
            return renderString(gson.toJson(request));
        } catch (IOException e) {
            throw new JsReportException(e);
        }
    }

    public String getServerVersion() {
        return null;
    }

    public Future<String> getServerVersionAsync() {
        return null;
    }

    public URI getServiceUri() {
        return uri;
    }

    public void setServiceUri(URI serviceUri) {
        this.uri = serviceUri;
    }


    // --- implementation focused methods

    public RegistryBuilder<ConnectionSocketFactory> getRegistryBuilder() {
        return registryBuilder;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    // --- private

    private Report renderString(String request) throws IOException, JsReportException {
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(new StringEntity(request));
        httpPost.setHeader(HEADER_CONTENT_TYPE, MIME_APPLICATION_JSON);

        // TODO - could be solved using HttpClient objects
        if (username != null && password != null) {
            String base64 = Base64.encodeBase64String(String.format("%s:%s", username, password).getBytes());
            httpPost.setHeader(HEADER_AUTHORIZATION, String.format("BASIC %s", base64));
        }

        // TODO - should be customized
        HttpClientConnectionManager cm = new BasicHttpClientConnectionManager(registryBuilder.build());
        HttpClient httpClient = HttpClients
                .custom()
                .setConnectionManager(cm)
                .build();

        HttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            throw new JsReportException(e);
        }

        if (response.getStatusLine().getStatusCode() != 200) {
            throw new JsReportException(String.format("Invalid status code (%d) !!!", response.getStatusLine().getStatusCode()));
        }

        Report result = new Report();

        try {
            result.setContent(response.getEntity().getContent());
        } catch (IOException e) {
            throw new JsReportException(e);
        }

        result.setContentType(findHeader(response, HEADER_CONTENT_TYPE));
        result.setFileExtension(findAndParseHeader(response, HEADER_FILE_EXTENSION));

        // TODO - how it works ?
        result.setPermanentLink(null);

        return result;
    }

    private Header findHeader(final HttpResponse response, final String headerName) {
        // TODO - may use exceptions
        assert response != null;
        Header[] headers = response.getHeaders(headerName);

        if (headers == null || headers.length < 1) {
            return null;
        }

        return headers[0];
    }

    private String findAndParseHeader(final HttpResponse response, final String headerName) {
        Header header = findHeader(response, headerName);
        return header == null ? null : header.getValue();
    }
}
