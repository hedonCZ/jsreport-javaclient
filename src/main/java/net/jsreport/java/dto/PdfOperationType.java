package net.jsreport.java.dto;

import com.google.gson.annotations.SerializedName;

public enum PdfOperationType {
    @SerializedName("append")
    APPEND,
    @SerializedName("prepend")
    PREPEND,
    @SerializedName("merge")
    MERGE,
}
