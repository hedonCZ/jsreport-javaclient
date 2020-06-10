package net.jsreport.java.service;

import net.jsreport.java.JsReportException;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.message.AbstractHttpMessage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

public class HttpRemoteServiceImpl implements HttpRemoteService {

    public static final String HEADER_FILE_EXTENSION = "File-Extension";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String MIME_APPLICATION_JSON = "application/json";

    private String baseServerUrl;

    private RegistryBuilder<ConnectionSocketFactory> registryBuilder;

    private String username;
    private String password;

    public HttpRemoteServiceImpl(String baseServerUrl) {
        this.baseServerUrl = baseServerUrl;
        registryBuilder = RegistryBuilder.create();

        if (baseServerUrl.startsWith("http://")) {
            registryBuilder.register("http", PlainConnectionSocketFactory.getSocketFactory());
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private void authorizeConnection(AbstractHttpMessage request) {
        // TODO - could be solved using HttpClient objects
        if (username != null && password != null) {
            String base64 = Base64.encodeBase64String(String.format("%s:%s", username, password).getBytes());
            request.setHeader(HEADER_AUTHORIZATION, String.format("BASIC %s", base64));
        }
    }

    private HttpResponse connectAndGetResponse(HttpUriRequest request) throws JsReportException {
        HttpClientConnectionManager cm = new BasicHttpClientConnectionManager(registryBuilder.build());
        HttpClient httpClient = HttpClients
                .custom()
                .setConnectionManager(cm)
                .build();

        HttpResponse response = null;

        try {
            response = httpClient.execute(request);
        } catch (IOException e) {
            throw new JsReportException(e);
        }

        return response;
    }

    public HttpResponse delete(String reqPath) throws URISyntaxException, JsReportException {
        HttpDelete httpDelete = new HttpDelete(new URI(baseServerUrl + reqPath));
        authorizeConnection(httpDelete);
        return connectAndGetResponse(httpDelete);
    }

    public HttpResponse post(String reqPath, String request) throws UnsupportedEncodingException, JsReportException, URISyntaxException {
        HttpPost httpPost = new HttpPost(new URI(baseServerUrl + reqPath));
        httpPost.setEntity(new StringEntity(request));
        httpPost.setHeader(HEADER_CONTENT_TYPE, MIME_APPLICATION_JSON);

        authorizeConnection(httpPost);
        return connectAndGetResponse(httpPost);
    }


    public Header findHeader(final HttpResponse response, final String headerName) {
        Header[] headers = response.getHeaders(headerName);

        if (headers == null || headers.length < 1) {
            return null;
        }

        return headers[0];
    }

    public String findAndParseHeader(final HttpResponse response, final String headerName) {
        Header header = findHeader(response, headerName);
        return header == null ? null : header.getValue();
    }
}
