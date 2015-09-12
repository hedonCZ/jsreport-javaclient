package net.jsreport.java;

public class JsReportException extends Throwable {

    public JsReportException() {
    }

    public JsReportException(String message) {
        super(message);
    }

    public JsReportException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsReportException(Throwable cause) {
        super(cause);
    }

    public JsReportException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
