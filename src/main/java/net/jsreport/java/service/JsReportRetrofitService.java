package net.jsreport.java.service;

import net.jsreport.java.rest.GetTemplateResponse;
import net.jsreport.java.rest.ListTemplateResponse;
import net.jsreport.java.rest.RenderRequest;
import net.jsreport.java.dto.Template;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

interface JsReportRetrofitService {

    @POST("/api/report")
    Call<ResponseBody> render(@Body RenderRequest request);

    @POST("/api/report")
    Call<ResponseBody> render(@Body Map<String, Object> request);

    @POST("/odata/templates")
    Call<Template> putTemplate(@Body Template template);

    @GET("/odata/templates({id})")
    Call<GetTemplateResponse> getTemplate(@Path("id") String id);

    @GET("/odata/templates")
    Call<ListTemplateResponse> listTemplates();

    @DELETE("/odata/templates({id})")
    Call<Void> removeTemplate(@Path("id") String id);

}
