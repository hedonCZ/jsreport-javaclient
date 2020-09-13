package net.jsreport.java.dto;

import com.google.gson.annotations.SerializedName;

public enum PhantomFormat {
    @SerializedName("A4")
    A4,
    @SerializedName("A5")
    A5,
    @SerializedName("A3")
    A3,
    @SerializedName("Letter")
    LETTER,
    @SerializedName("Tabloid")
    TABLOID,
    @SerializedName("Legal")
    Legal,
}
