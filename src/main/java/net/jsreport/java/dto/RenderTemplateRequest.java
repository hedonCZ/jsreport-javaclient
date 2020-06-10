package net.jsreport.java.dto;

import net.jsreport.java.entity.Template;

/**
 *
 */
public class RenderTemplateRequest {

    private Template template;

    private Object data;

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
}
