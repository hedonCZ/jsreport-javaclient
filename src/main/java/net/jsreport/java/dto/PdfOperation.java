package net.jsreport.java.dto;

public class PdfOperation {
    private PdfOperationType type;
    private Boolean mergeToFront;
    private Boolean renderForEveryPage;
    private Boolean mergeWholeDocument;
    private String templateShortid;

    public PdfOperationType getType() {
        return type;
    }

    public void setType(PdfOperationType type) {
        this.type = type;
    }

    public Boolean getMergeToFront() {
        return mergeToFront;
    }

    public void setMergeToFront(Boolean mergeToFront) {
        this.mergeToFront = mergeToFront;
    }

    public Boolean getRenderForEveryPage() {
        return renderForEveryPage;
    }

    public void setRenderForEveryPage(Boolean renderForEveryPage) {
        this.renderForEveryPage = renderForEveryPage;
    }

    public Boolean getMergeWholeDocument() {
        return mergeWholeDocument;
    }

    public void setMergeWholeDocument(Boolean mergeWholeDocument) {
        this.mergeWholeDocument = mergeWholeDocument;
    }

    public String getTemplateShortid() {
        return templateShortid;
    }

    public void setTemplateShortid(String templateShortid) {
        this.templateShortid = templateShortid;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    private Template template;
}
