package net.jsreport.java.dto;

public class HtmlToXlsx {
    private String htmlEngine;
    private String templateAssetShortid;
    private Asset templateAsset;

    public String getHtmlEngine() {
        return htmlEngine;
    }

    public void setHtmlEngine(String htmlEngine) {
        this.htmlEngine = htmlEngine;
    }

    public String getTemplateAssetShortid() {
        return templateAssetShortid;
    }

    public void setTemplateAssetShortid(String templateAssetShortid) {
        this.templateAssetShortid = templateAssetShortid;
    }

    public Asset getTemplateAsset() {
        return templateAsset;
    }

    public void setTemplateAsset(Asset templateAsset) {
        this.templateAsset = templateAsset;
    }
}
