package net.jsreport.java.entity;

import org.apache.http.Header;

import java.io.InputStream;
import java.io.OutputStream;

public class Report {

    /**
     * Stream with report
     */
    private InputStream content;

    /**
     * Report content type like application/pdf
     */
    private Header ContentType;

    /**
     * Report file extension like "html" or "pdf"
     */
    private String FileExtension;

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

    public Header getContentType() {
        return ContentType;
    }

    public void setContentType(Header contentType) {
        ContentType = contentType;
    }

    public String getFileExtension() {
        return FileExtension;
    }

    public void setFileExtension(String fileExtension) {
        FileExtension = fileExtension;
    }

    public String getPermanentLink() {
        return permanentLink;
    }

    public void setPermanentLink(String permanentLink) {
        this.permanentLink = permanentLink;
    }
}
