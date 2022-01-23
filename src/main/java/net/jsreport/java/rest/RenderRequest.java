package net.jsreport.java.rest;

import net.jsreport.java.dto.Options;
import net.jsreport.java.dto.Template;

public class RenderRequest {

    public RenderRequest() {

    }

    public RenderRequest(Template template, Object data, Options options) {
        this.template = template;
        this.data = data;
        this.options = options;
    }

    public RenderRequest(Template template, Object data) {
        this.template = template;
        this.data = data;
    }

    private Template template;

    private Object data;

    private Options options;

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }
}
