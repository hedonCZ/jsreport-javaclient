package net.jsreport.java.service;

import com.google.gson.Gson;
import net.jsreport.java.JsReportException;
import net.jsreport.java.dto.RenderTemplateRequest;
import net.jsreport.java.entity.Report;
import net.jsreport.java.entity.Template;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.net.URISyntaxException;

public class ReportingServiceImpl implements ReportingService {

    private static final Gson GSON = new Gson();

    private final HttpRemoteService remoteService;

    public ReportingServiceImpl(HttpRemoteService remoteService) {
        this.remoteService = remoteService;
    }

    public Report render(String templateShortid, Object data) throws JsReportException {
        Template template = new Template();
        template.setShortid(templateShortid);

        RenderTemplateRequest renderRequest = new RenderTemplateRequest();
        renderRequest.setTemplate(template);
        renderRequest.setData(data);

        return render(renderRequest);
    }

    public Report render(String templateShortid, String jsonData) throws JsReportException {
        String requestString = String.format("{ \"template\" : { \"shortid\" : \"%s\"}, \"data\" : %s }", templateShortid, jsonData);

        try {
            return renderString(requestString);
        } catch (IOException | URISyntaxException e) {
            throw new JsReportException(e);
        }
    }

    public Report render(RenderTemplateRequest request) throws JsReportException {
        try {
            return renderString(GSON.toJson(request));
        } catch (IOException | URISyntaxException e) {
            throw new JsReportException(e);
        }
    }

    // --- private

    private Report renderString(String request) throws IOException, JsReportException, URISyntaxException {
        HttpResponse response = remoteService.post("/api/report", request);

        if (response.getStatusLine().getStatusCode() >= 400) {
            throw new JsReportException(String.format("Invalid status code (%d) !!!", response.getStatusLine().getStatusCode()));
        }

        Report result = new Report();

        try {
            result.setContent(response.getEntity().getContent());
        } catch (IOException e) {
            throw new JsReportException(e);
        }

        result.setContentType(remoteService.findHeader(response, HttpRemoteServiceImpl.HEADER_CONTENT_TYPE));
        result.setFileExtension(remoteService.findAndParseHeader(response, HttpRemoteServiceImpl.HEADER_FILE_EXTENSION));
        // TODO
        result.setPermanentLink(null);

        return result;
    }
}
