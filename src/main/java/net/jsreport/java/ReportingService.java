package net.jsreport.java;

import java.net.URI;
import java.util.concurrent.Future;

public interface ReportingService {

    /**
     * The simpliest rendering using template shortid and input data
     *
     * @param templateShortid can be taken from jsreport studio or from filename in jsreport embedded
     * @param data any json serializable object
     * @throws Exception
     * <exception cref="JsReportException"></exception>
     *
     * @return Report result promise
     *
     * */
    Future<Report> renderAsync(String templateShortid, Object data) throws JsReportException;
    Report render(String templateShortid, Object data) throws JsReportException;

    /**
     * The simpliest rendering using template shortid and input data
     *
     * @param templateShortid template shortid can be taken from jsreport studio or from filename in jsreport embedded
     * @param jsonData any json String
     * @throws Exception
     * <exception cref="JsReportException"></exception>
     *
     * @return Report result promise
     * */
    Future<Report> renderAsync(String templateShortid, String jsonData) throws JsReportException;
    Report render(String templateShortid, String jsonData) throws JsReportException;

    /**
     * Overload for more sophisticated rendering.
     *
     * @param request Description of rendering process {@link RenderRequest}
     *
     * @throws Exception
     * /// <exception cref="JsReportException"></exception>
     *
     * @return Report result promise
     * */
    Future<Report> renderAsync(RenderRequest request);
    Report render(RenderRequest request) throws JsReportException;

    /**
     * Request jsreport package version
     * */
    Future<String> getServerVersionAsync();
    String getServerVersion();


    /***
     * Uri to jsreport server like http://localhost:2000/ or https://subdomain.jsreportonline.net
     */
    public URI getServiceUri() ;

    /** @see {@link #getServiceUri()} */
    public void setServiceUri(URI serviceUri);
}
