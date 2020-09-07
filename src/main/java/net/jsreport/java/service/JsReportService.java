package net.jsreport.java.service;

import net.jsreport.java.JsReportException;
import net.jsreport.java.dto.CreateTemplateRequest;
import net.jsreport.java.dto.RenderTemplateRequest;
import net.jsreport.java.entity.Report;
import net.jsreport.java.entity.Template;
import org.apache.http.Header;
import org.apache.http.HttpResponse;

public interface JsReportService {
    Report render(RenderTemplateRequest renderTemplateRequest) throws JsReportException;

    Template putTemplate(CreateTemplateRequest createTemplateRequest) throws JsReportException;

    void removeTemplate(String id) throws JsReportException;

}
