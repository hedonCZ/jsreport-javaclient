package net.jsreport.java.service;

import net.jsreport.java.JsReportException;
import org.apache.http.Header;
import org.apache.http.HttpResponse;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

public interface HttpRemoteService {
    HttpResponse delete(String reqPath) throws URISyntaxException, JsReportException;

    HttpResponse post(String reqPath, String request) throws UnsupportedEncodingException, JsReportException, URISyntaxException;

    Header findHeader(HttpResponse response, String headerName);

    String findAndParseHeader(HttpResponse response, String headerName);
}
