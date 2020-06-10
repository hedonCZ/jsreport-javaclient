package net.jsreport.java.service;

import net.jsreport.java.JsReportException;
import net.jsreport.java.entity.Template;
import net.jsreport.java.entity.TemplateRequest;

public interface TemplateService {

    Template putTemplate(TemplateRequest request) throws JsReportException;

    void removeTemplate(String id) throws JsReportException;
}
