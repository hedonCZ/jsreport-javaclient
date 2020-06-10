package net.jsreport.java.service;

import net.jsreport.java.JsReportException;
import net.jsreport.java.entity.Template;
import net.jsreport.java.dto.CreateTemplateRequest;

public interface TemplateService {

    Template putTemplate(CreateTemplateRequest request) throws JsReportException;

    void removeTemplate(String id) throws JsReportException;
}
