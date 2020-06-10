package net.jsreport.java.service;

import com.google.gson.Gson;
import net.jsreport.java.JsReportException;
import net.jsreport.java.entity.Template;
import net.jsreport.java.entity.TemplateRequest;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

public class TemplateServiceImpl extends HttpService implements TemplateService {

    private Gson gson = new Gson();

    public TemplateServiceImpl(String baseServerUrl) {
        super(baseServerUrl);
    }

    public Template putTemplate(TemplateRequest request) throws JsReportException {
        HttpResponse response = null;
        try {
            response = post("/odata/templates", gson.toJson(request));
        } catch (Exception e) {
            throw new JsReportException(e);
        }

        if (response.getStatusLine().getStatusCode() >= 300) {
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
            response = delete("/odata/templates(" + id + ")");
        } catch (URISyntaxException e) {
            throw new JsReportException(e);
        }

        if (response.getStatusLine().getStatusCode() >= 300) {
            throw new JsReportException(String.format("Invalid status code (%d) !!!", response.getStatusLine().getStatusCode()));
        }

    }
}
