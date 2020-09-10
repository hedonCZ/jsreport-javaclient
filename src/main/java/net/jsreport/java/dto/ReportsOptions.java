package net.jsreport.java.dto;

public class ReportsOptions {
    private Boolean save;

    public Boolean getSave() {
        return save;
    }

    public void setSave(Boolean save) {
        this.save = save;
    }

    public Boolean getAsync() {
        return async;
    }

    public void setAsync(Boolean async) {
        this.async = async;
    }

    private Boolean async;
}
