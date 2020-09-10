package net.jsreport.java.dto;

public class PdfSign {
    private String Reason;
    private CertificateAsset certificateAsset;

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public CertificateAsset getCertificateAsset() {
        return certificateAsset;
    }

    public void setCertificateAsset(CertificateAsset certificateAsset) {
        this.certificateAsset = certificateAsset;
    }
}
