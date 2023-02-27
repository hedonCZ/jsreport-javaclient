package net.jsreport.java.dto;

/**
 * Addition
 * */
public class Options {

    private Boolean preview;

    private Integer timeout;

    private String base;

    private String language;

    private ReportsOptions reports;

    private String reportName;

    private LocalizationOptions localization;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public ReportsOptions getReports() {
        return reports;
    }

    public void setReports(ReportsOptions reports) {
        this.reports = reports;
    }

    public LocalizationOptions getLocalization() {
        return this.localization;
    }

     public void setLocalization(LocalizationOptions localization) {
        this.localization = localization;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public Boolean getPreview() {
        return preview;
    }

    public void setPreview(Boolean preview) {
        this.preview = preview;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }
}
