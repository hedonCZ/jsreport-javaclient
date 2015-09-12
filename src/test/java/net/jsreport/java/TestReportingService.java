package net.jsreport.java;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static org.testng.Assert.assertNotNull;

/**
 */
public class TestReportingService {

    private static final String LOCALHOST_SHORT_TEMPLATE_ID = "mySObORM";
    private static final String LOCALHOST_REMOTE_SERVICE_URI = "https://localhost:8443/api/report";
    private static final String TESTING_JSON_DATA_FILE = "test-data-part.json";

    private URI localhostRemoteService;

    private ReportingServiceImpl reportingService;

    private Map<String, Object> data;

    private SSLConnectionSocketFactory sslcf;

    public TestReportingService() {
    }


    @Test
    public void testRenderSyncObject() throws IOException, JsReportException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        reportingService.setServiceUri(localhostRemoteService);
        reportingService.getRegistryBuilder().register("https", sslcf);

        Report render = reportingService.render(LOCALHOST_SHORT_TEMPLATE_ID, data);
        assertReport(render);
    }

    @Test
    public void testRenderSyncString() throws IOException, JsReportException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        reportingService.setServiceUri(localhostRemoteService);
        reportingService.getRegistryBuilder().register("https", sslcf);

        Report render = reportingService.render(LOCALHOST_SHORT_TEMPLATE_ID, readFileToString(TESTING_JSON_DATA_FILE));

        assertReport(render);
    }

    // TODO - sync with template
    // TODO - all async tests

    /**
     * Assert report's properties.
     *
     * Permanent link is not asserted. Behaviour of permanent link may change in future.
     * */
    private void assertReport(Report render) {
        assertNotNull(render);
        assertNotNull(render.getContent());
        assertNotNull(render.getContentType());
        assertNotNull(render.getFileExtension());
    }

    /**
     * Prepares factory for testing.
     *
     *
     * @see http://stackoverflow.com/questions/19517538/ignoring-ssl-certificate-in-apache-httpclient-4-3 for details.
     */
    @BeforeTest
    private void setupSslConnectionFactory() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        sslcf = new SSLConnectionSocketFactory(builder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    }

    @BeforeTest
    public void setUpService() throws URISyntaxException {
        reportingService = new ReportingServiceImpl();
        localhostRemoteService = new URI(LOCALHOST_REMOTE_SERVICE_URI);

        reportingService.setUsername("admin");
        reportingService.setPassword("password");
    }

//    TODO - simplified
    @BeforeTest
    public void setUpReportData() {
        Map<String, String> customerMap = new HashMap<String, String>();
        customerMap.put("companyName", "tbd - cust.companyName");
        customerMap.put("name", "tbd - cust.name");
        customerMap.put("surname", "tbd - cust.surname");
        customerMap.put("street", "tbd - cust.street");
        customerMap.put("city", "tbd - cust.city");
        customerMap.put("postal", "tbd - cust.postal");
        customerMap.put("id", "tbd - cust.id");
        customerMap.put("vatid", "tbd - cust.vatid");

        Map<String, String> invoiceMap = new HashMap<String, String>();
        invoiceMap.put("variableSymbol", "tbd - variableSymbol");
        invoiceMap.put("specificSymbol", "tbd - specificSymbol");
        invoiceMap.put("constantSymbol", "tbd - constantSymbol");
        invoiceMap.put("price", "tbd - price");
        invoiceMap.put("created", "tbd - created");
        invoiceMap.put("issue", "tbd - issue");

        List<Map> items = new ArrayList<Map>();
        Map<String, String> item1 = new HashMap<String, String>();
        item1.put("name", "itm#1 - name");
        item1.put("price", "itm#1 - price");
        item1.put("currencySymbol", "K\u010D");

        Map<String, String> item2 = new HashMap<String, String>();
        item2.put("name", "itm#2 - name");
        item2.put("price", "itm#2 - price");
        item2.put("currencySymbol", "K\u010D");

        items.add(item1);
        items.add(item2);

        data = new HashMap<String, Object>();
        data.put("customer", customerMap);
        data.put("invoice", invoiceMap);
        data.put("items", items);
    }

    @BeforeTest
    public void setUpReportTemplate() {
    }

    private String readFileToString(String filename) throws FileNotFoundException {
        return new Scanner(getClass().getClassLoader().getResourceAsStream(filename)).useDelimiter("\\Z").next();
    }
}
