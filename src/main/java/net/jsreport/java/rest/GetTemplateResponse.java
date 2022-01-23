package net.jsreport.java.rest;

import com.google.gson.annotations.SerializedName;
import net.jsreport.java.dto.Template;

import java.util.List;

public class GetTemplateResponse {

    @SerializedName("@odata.context")
    private String odataContext;

    @SerializedName("0")
    private Template template;

    private List<Template> value;

    public String getOdataContext() {
        return odataContext;
    }

    public void setOdataContext(String odataContext) {
        this.odataContext = odataContext;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public List<Template> getValue() {
        return value;
    }

    public void setValue(List<Template> value) {
        this.value = value;
    }
}
