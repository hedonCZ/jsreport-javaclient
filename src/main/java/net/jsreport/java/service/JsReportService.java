package net.jsreport.java.service;

import net.jsreport.java.JsReportException;
import net.jsreport.java.dto.Options;
import net.jsreport.java.dto.RenderRequest;
import net.jsreport.java.dto.Report;
import net.jsreport.java.dto.Template;

import java.util.Map;
import java.util.concurrent.Future;

public interface JsReportService {
    Report render(RenderRequest renderRequest) throws JsReportException;
    Report render(Map<String, Object> renderRequest) throws JsReportException;
    Report render(String templateName, Object data) throws JsReportException;
    Report render(String templateName, Object data, Options options) throws JsReportException;

    Template putTemplate(Template template) throws JsReportException;

    void removeTemplate(String id) throws JsReportException;

    Future<Report> renderAsync(RenderRequest renderTemplateRequest);

}
