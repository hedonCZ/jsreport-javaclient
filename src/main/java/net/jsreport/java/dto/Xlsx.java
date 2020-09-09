package net.jsreport.java.dto;

public class Xlsx {
    private String templateAssetShortid;

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

    private Asset templateAsset;
}
