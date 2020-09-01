package net.jsreport.java.service;

import net.jsreport.java.JsReportException;
import org.apache.http.Header;
import org.apache.http.HttpResponse;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

public interface HttpRemoteService {
    HttpResponse delete(String reqPath) throws URISyntaxException, JsReportException;

    HttpResponse post(String reqPath, String request) throws UnsupportedEncodingException, JsReportException, URISyntaxException;

    default Header findHeader(HttpResponse response, String headerName) {
        Header[] headers = response.getHeaders(headerName);

        if (headers == null || headers.length < 1) {
            return null;
        }

        return headers[0];
    }

    default String findAndParseHeader(HttpResponse response, String headerName) {
        Header header = findHeader(response, headerName);
        return header == null ? null : header.getValue();
    }
}
