package net.jsreport.java.dto;

import com.google.gson.annotations.SerializedName;

public enum Engine {
    @SerializedName("handlebars")
    HANDLEBARS,
    @SerializedName("jsrender")
    JSRENDER,
    @SerializedName("none")
    NONE,
    @SerializedName("pug")
    PUG,
    @SerializedName("ejs")
    EJS
}
