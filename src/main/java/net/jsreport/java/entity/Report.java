package net.jsreport.java.entity;

import java.io.InputStream;

public class Report {

    /**
     * Stream with report
     */
    private InputStream content;

    /**
     * Report content type like application/pdf
     */
    private String contentType;

    /**
     * Report file extension like "html" or "pdf"
     */
    private String fileExtension;

    /**
     * Optional pernament link to report
     */
    private String permanentLink;

    public InputStream getContent() {
        return content;
    }

    public void setContent(InputStream content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getPermanentLink() {
        return permanentLink;
    }

    public void setPermanentLink(String permanentLink) {
        this.permanentLink = permanentLink;
    }
}
