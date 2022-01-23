package net.jsreport.java.rest;

import com.google.gson.annotations.SerializedName;
import net.jsreport.java.dto.Template;

import java.util.List;

public class ListTemplateResponse {

    @SerializedName("@odata.context")
    private String odataContext;

    private List<Template> value;

    public String getOdataContext() {
        return odataContext;
    }

    public void setOdataContext(String odataContext) {
        this.odataContext = odataContext;
    }

    public List<Template> getValue() {
        return value;
    }

    public void setValue(List<Template> value) {
        this.value = value;
    }
}
