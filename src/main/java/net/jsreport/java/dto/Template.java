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
    private String engine;

    /**
     * Used recipe defining rendering process like "html", "phantom-pdf" or "fop"
     * */
    private String recipe;

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

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
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
}
