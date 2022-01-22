package net.jsreport.java.dto;

import java.util.List;

public class Template {

    public Template() {

    }

    public Template (String name) {
        this.name = name;
    }

    private String _id;

    /**
     * Unique 9 alfanum id
     */
    private String shortid;

    /**
     * Content of report, most often this is html with javasript templating engines
     */
    private String content;

    /**
     * Javascript helper functions in format: function a() { }; function b() { };
     * */
    private String helpers;

    /**
     * Used javascript templating engine like "jsrender" or "handlebars"
     * */
    private Engine engine;

    /**
     * Used recipe defining rendering process like "html", "phantom-pdf" or "fop"
     * */
    private Recipe recipe;

    /**
     * Readable name, does not need to be unique
     * */
    private String name;

    /**
     * Optional specification for phantom-pdf
     * */
    private Phantom phantom;

    private Chrome chrome;
    private Docx docx;
    private ChromeImage chromeImage;
    private HtmlToXlsx htmlToXlsx;
    private OfficePassword officePassword;
    private PdfMeta pdfMeta;
    private List<PdfOperation> pdfOperations;
    private PdfPassword pdfPassword;
    private PdfSign pdfSign;
    private Pptx pptx;
    private List<Script> scripts;
    private StaticPdf staticPdf;
    private Xlsx xlsx;

    public Docx getDocx() {
        return docx;
    }

    public void setDocx(Docx docx) {
        this.docx = docx;
    }

    public Chrome getChrome() {
        return chrome;
    }

    public void setChrome(Chrome chrome) {
        this.chrome = chrome;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getShortid() {
        return shortid;
    }

    public void setShortid(String shortid) {
        this.shortid = shortid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHelpers() {
        return helpers;
    }

    public void setHelpers(String helpers) {
        this.helpers = helpers;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Phantom getPhantom() {
        return phantom;
    }

    public void setPhantom(Phantom phantom) {
        this.phantom = phantom;
    }

    public ChromeImage getChromeImage() {
        return chromeImage;
    }

    public void setChromeImage(ChromeImage chromeImage) {
        this.chromeImage = chromeImage;
    }

    public HtmlToXlsx getHtmlToXlsx() {
        return htmlToXlsx;
    }

    public void setHtmlToXlsx(HtmlToXlsx htmlToXlsx) {
        this.htmlToXlsx = htmlToXlsx;
    }

    public OfficePassword getOfficePassword() {
        return officePassword;
    }

    public void setOfficePassword(OfficePassword officePassword) {
        this.officePassword = officePassword;
    }

    public PdfMeta getPdfMeta() {
        return pdfMeta;
    }

    public void setPdfMeta(PdfMeta pdfMeta) {
        this.pdfMeta = pdfMeta;
    }

    public List<PdfOperation> getPdfOperations() {
        return pdfOperations;
    }

    public void setPdfOperations(List<PdfOperation> pdfOperations) {
        this.pdfOperations = pdfOperations;
    }

    public PdfPassword getPdfPassword() {
        return pdfPassword;
    }

    public void setPdfPassword(PdfPassword pdfPassword) {
        this.pdfPassword = pdfPassword;
    }

    public PdfSign getPdfSign() {
        return pdfSign;
    }

    public void setPdfSign(PdfSign pdfSign) {
        this.pdfSign = pdfSign;
    }

    public Pptx getPptx() {
        return pptx;
    }

    public void setPptx(Pptx pptx) {
        this.pptx = pptx;
    }

    public List<Script> getScripts() {
        return scripts;
    }

    public void setScripts(List<Script> scripts) {
        this.scripts = scripts;
    }

    public StaticPdf getStaticPdf() {
        return staticPdf;
    }

    public void setStaticPdf(StaticPdf staticPdf) {
        this.staticPdf = staticPdf;
    }

    public Xlsx getXlsx() {
        return xlsx;
    }

    public void setXlsx(Xlsx xlsx) {
        this.xlsx = xlsx;
    }
}
