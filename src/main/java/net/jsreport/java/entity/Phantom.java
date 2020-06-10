package net.jsreport.java.entity;

/**
 * Complex object used to configure phantom-pdf rendering. It's embedded in Template
 */
public class Phantom {

    /**
     * Paper margin like 2cm, see phantom doc for details http://phantomjs.org/
     */
    private String margin;

    /**
     * Html used for page header, you can use special tags to insert page numbers: {#pageNum}/{#numPages}
     */
    private String header;


    /**
     * Height of header like 2cm
     */
    private String headerHeight;

    /**
     * Html used for page footer, you can use special tags to insert page numbers: {#pageNum}/{#numPages}
     */
    private String footer;

    /**
     * Height of footer like 2cm
     */
    private String footerHeight;

    /**
     * Paper orientation, possible values "landscape" and "portrait"
     */
    private String orientation;

    /**
     * Paper format, possible values "A5", "A4", "A3", "Letter", "Tabloid", "Legal"
     * width or height specification takes precedence
     */
    private String format;

    /**
     * Paper width like 2cm
     */
    private String width;

    /**
     * Paper height like 2cm
     */
    private String height;

    // --- getters & setters ---

    public String getMargin() {
        return margin;
    }

    public void setMargin(String margin) {
        this.margin = margin;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getHeaderHeight() {
        return headerHeight;
    }

    public void setHeaderHeight(String headerHeight) {
        this.headerHeight = headerHeight;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getFooterHeight() {
        return footerHeight;
    }

    public void setFooterHeight(String footerHeight) {
        this.footerHeight = footerHeight;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
