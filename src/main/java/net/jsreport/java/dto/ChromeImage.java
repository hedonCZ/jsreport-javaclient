package net.jsreport.java.dto;

import java.math.BigDecimal;

public class ChromeImage {
    private String type;
    private BigDecimal quality;
    private Boolean fullPage;
    private BigDecimal clipX;
    private BigDecimal clipY;
    private BigDecimal clipHeight;
    private BigDecimal clipWidth;
    private Boolean omitBackground;
    private MediaType mediaType;
    private Boolean waitForJS;
    private boolean waitForNetworkIddle;
    private String url;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getQuality() {
        return quality;
    }

    public void setQuality(BigDecimal quality) {
        this.quality = quality;
    }

    public Boolean getFullPage() {
        return fullPage;
    }

    public void setFullPage(Boolean fullPage) {
        this.fullPage = fullPage;
    }

    public BigDecimal getClipX() {
        return clipX;
    }

    public void setClipX(BigDecimal clipX) {
        this.clipX = clipX;
    }

    public BigDecimal getClipY() {
        return clipY;
    }

    public void setClipY(BigDecimal clipY) {
        this.clipY = clipY;
    }

    public BigDecimal getClipHeight() {
        return clipHeight;
    }

    public void setClipHeight(BigDecimal clipHeight) {
        this.clipHeight = clipHeight;
    }

    public BigDecimal getClipWidth() {
        return clipWidth;
    }

    public void setClipWidth(BigDecimal clipWidth) {
        this.clipWidth = clipWidth;
    }

    public Boolean getOmitBackground() {
        return omitBackground;
    }

    public void setOmitBackground(Boolean omitBackground) {
        this.omitBackground = omitBackground;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public Boolean getWaitForJS() {
        return waitForJS;
    }

    public void setWaitForJS(Boolean waitForJS) {
        this.waitForJS = waitForJS;
    }

    public boolean isWaitForNetworkIddle() {
        return waitForNetworkIddle;
    }

    public void setWaitForNetworkIddle(boolean waitForNetworkIddle) {
        this.waitForNetworkIddle = waitForNetworkIddle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
