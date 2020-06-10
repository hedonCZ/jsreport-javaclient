package net.jsreport.java;

public interface TemplateService {

    Template putTemplate(TemplateRequest request) throws JsReportException;

    void removeTemplate(String id) throws JsReportException;
}
