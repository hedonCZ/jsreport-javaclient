package net.jsreport.java.service;

import net.jsreport.java.dto.CreateTemplateRequest;
import net.jsreport.java.dto.RenderTemplateRequest;
import net.jsreport.java.entity.Template;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;

interface JsReportRetrofitService {

    @POST("/api/report")
    Call<ResponseBody> render(@Body RenderTemplateRequest request);

    @POST("/odata/templates")
    Call<Template> putTemplate(@Body CreateTemplateRequest createTemplateRequest);

    @DELETE("/odata/templates({id})")
    Call<Void> removeTemplate(@Path("id") String id);

}
