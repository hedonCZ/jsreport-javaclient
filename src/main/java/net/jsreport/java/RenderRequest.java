package net.jsreport.java;

/**
 *
 */
public class RenderRequest {

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
