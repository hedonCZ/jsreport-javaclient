package net.jsreport.java.dto;

public class StaticPdf {
    private String pdfAssetShortid;

    public String getPdfAssetShortid() {
        return pdfAssetShortid;
    }

    public void setPdfAssetShortid(String pdfAssetShortid) {
        this.pdfAssetShortid = pdfAssetShortid;
    }

    public Asset getPdfAsset() {
        return pdfAsset;
    }

    public void setPdfAsset(Asset pdfAsset) {
        this.pdfAsset = pdfAsset;
    }

    private Asset pdfAsset;
}
