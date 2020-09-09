package net.jsreport.java.service;

import net.jsreport.java.dto.RenderRequest;
import net.jsreport.java.dto.Template;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.Map;

interface JsReportRetrofitService {

    @POST("/api/report")
    Call<ResponseBody> render(@Body RenderRequest request);

    @POST("/api/report")
    Call<ResponseBody> render(@Body Map<String, Object> request);

    @POST("/odata/templates")
    Call<Template> putTemplate(@Body Template template);

    @DELETE("/odata/templates({id})")
    Call<Void> removeTemplate(@Path("id") String id);

}
