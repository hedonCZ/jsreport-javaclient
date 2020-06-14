package net.jsreport.java.service;

import com.google.gson.Gson;
import net.jsreport.java.JsReportException;
import net.jsreport.java.entity.Template;
import net.jsreport.java.dto.CreateTemplateRequest;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

public class TemplateServiceImpl implements TemplateService {

    private Gson gson = new Gson();

    private HttpRemoteService httpRemoteService;

    public TemplateServiceImpl(HttpRemoteService httpRemoteService) {
        this.httpRemoteService = httpRemoteService;
    }

    public Template putTemplate(CreateTemplateRequest request) throws JsReportException {
        HttpResponse response = null;
        try {
            response = httpRemoteService.post("/odata/templates", gson.toJson(request));
        } catch (Exception e) {
            throw new JsReportException(e);
        }

        if (response.getStatusLine().getStatusCode() >= 400) {
            throw new JsReportException(String.format("Invalid status code (%d) !!!", response.getStatusLine().getStatusCode()));
        }

        try {
            return new Gson().fromJson(new InputStreamReader(response.getEntity().getContent()), Template.class);
        } catch (IOException e) {
            throw new JsReportException(e);
        }
    }

    public void removeTemplate(String id) throws JsReportException {
        HttpResponse response = null;

        try {
            response = httpRemoteService.delete("/odata/templates(" + id + ")");
        } catch (URISyntaxException e) {
            throw new JsReportException(e);
        }

        if (response.getStatusLine().getStatusCode() >= 400) {
            throw new JsReportException(String.format("Invalid status code (%d) !!!", response.getStatusLine().getStatusCode()));
        }

    }
}
