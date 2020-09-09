package net.jsreport.java.dto;

public class Docx {
    private String templateAssetShortid;
    private Asset templateAsset;

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
