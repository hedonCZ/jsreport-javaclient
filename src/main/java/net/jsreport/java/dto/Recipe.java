package net.jsreport.java.dto;

import com.google.gson.annotations.SerializedName;

public enum Recipe {
    @SerializedName("html")
    HTML,
    @SerializedName("chrome-pdf")
    CHROME_PDF,
    @SerializedName("phantom-pdf")
    PHANTOM_PDF,
    @SerializedName("fop-pdf")
    FOP_PDF,
    @SerializedName("text")
    TEXT,
    @SerializedName("html-with-browser-client")
    HTML_WITH_BROWSER_CLIENT,
    @SerializedName("html-to-xlsx")
    HTML_TO_XLSX,
    @SerializedName("xlsx")
    XLSX,
    @SerializedName("electron-pdf")
    ELECTRON_PDF,
    @SerializedName("html-to-text")
    HTML_TO_TEXT,
    @SerializedName("wkhtmltopdf")
    WKHTMLTOPDF,
    @SerializedName("phantom-image")
    PHANTOM_IMAGE,
    @SerializedName("chrome-image")
    CHROME_IMAGE,
    @SerializedName("docx")
    DOCX,
    @SerializedName("pptx")
    PPTX,
    @SerializedName("static-pdf")
    STATIC_PDF
}

