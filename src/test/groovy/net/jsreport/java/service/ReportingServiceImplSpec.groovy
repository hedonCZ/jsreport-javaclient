package net.jsreport.java.service

import com.google.gson.Gson
import net.jsreport.java.JsReportException
import net.jsreport.java.dto.RenderTemplateRequest
import net.jsreport.java.entity.Report
import net.jsreport.java.entity.Template
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.ProtocolVersion
import org.apache.http.message.BasicHeader
import org.apache.http.message.BasicStatusLine
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import spock.lang.Specification
import spock.lang.Unroll

class ReportingServiceImplSpec extends Specification {

    private static final Gson GSON = new Gson()
    private static final PDFTextStripper PDF_TEXT_STRIPPER = new PDFTextStripper()
    private static final BasicHeader CONTENT_TYPE_HEADER = new BasicHeader(HttpRemoteServiceImpl.HEADER_CONTENT_TYPE, "")

    @Unroll
    def testRender() {
        setup:

        ReportingService reportingService = new ReportingServiceImpl(HTTP_REMOTE_SERVICE_MOCK)

        when:

        RenderTemplateRequest renderTemplateRequest = new RenderTemplateRequest(template: new Template(name: "${name}"))
        Report report = null
        try {
            report = reportingService.render(renderTemplateRequest)
        } catch(Throwable e) {
            if (name.startsWith("code_")) return
            if (name.contains(e.class.simpleName)) return
            if (e.cause != null && name.contains(e.cause.class.simpleName)) return

            assert false : "Invalid exception"
        }

        then:

        assertReport(report, text)

        where:

        name                                    | text                                                          | data
        "ok"                                    | "Simple test of template call!${System.lineSeparator()}"      | null
        "data"                                  | "Hello jsreport!${System.lineSeparator()}"                    | [ "user" : "jsreport" ]
        "ex_UnsupportedEncodingException"       | null                                                          | null
        "ex_JsReportException"                  | null                                                          | null
        "ic_IOException"                        | null                                                          | null
        "ic_UnsupportedOperationException"      | null                                                          | null
        "code_500"                              | null                                                          | null
        "code_404"                              | null                                                          | null
        "code_400"                              | null                                                          | null
    }

    @Unroll
    def testRenderByShortIdAndSerializedJson() {
        when:

        ReportingService reportingService = new ReportingServiceImpl(HTTP_REMOTE_SERVICE_MOCK)

        then:

        Report report = null
        try {
            report = reportingService.render(name, GSON.toJson(data))
        } catch(Throwable e) {
            if (name.startsWith("code_")) return
            if (name.contains(e.class.simpleName)) return
            if (e.cause != null && name.contains(e.cause.class.simpleName)) return

            assert false : "Invalid exception"
        }

        assertReport(report, text)

        where:

        name                                    | text                                                          | data
        "ok"                                    | "Simple test of template call!${System.lineSeparator()}"      | null
        "data"                                  | "Hello jsreport!${System.lineSeparator()}"                    | [ "user" : "jsreport" ]
        "ex_UnsupportedEncodingException"       | null                                                          | null
        "ex_URISyntaxException"                 | null                                                          | null
        "ex_JsReportException"                  | null                                                          | null
        "ic_IOException"                        | null                                                          | null
        "ic_UnsupportedOperationException"      | null                                                          | null
        "code_500"                              | null                                                          | null
        "code_404"                              | null                                                          | null
        "code_400"                              | null                                                          | null
    }

    @Unroll
    def testRenderByShortIdAndObject() {
        when:

        ReportingService reportingService = new ReportingServiceImpl(HTTP_REMOTE_SERVICE_MOCK)

        then:

        Report report = null
        try {
            report = reportingService.render(name, data)
        } catch(Throwable e) {
            if (name.startsWith("code_")) return
            if (name.contains(e.class.simpleName)) return
            if (e.cause != null && name.contains(e.cause.class.simpleName)) return

            assert false : "Invalid exception"
        }

        assertReport(report, text)

        where:

        name                                    | text                                                      | data
        "ok"                                    | "Simple test of template call!${System.lineSeparator()}"  | null
        "data"                                  | "Hello jsreport!${System.lineSeparator()}"                | [ "user" : "jsreport" ]
        "ex_UnsupportedEncodingException"       | null                                                      | null
        "ex_URISyntaxException"                 | null                                                      | null
        "ex_JsReportException"                  | null                                                      | null
        "ic_IOException"                        | null                                                      | null
        "ic_UnsupportedOperationException"      | null                                                      | null
        "code_500"                              | null                                                      | null
        "code_404"                              | null                                                      | null
        "code_400"                              | null                                                      | null
    }

    //
    // Mocks
    //

    HttpRemoteService HTTP_REMOTE_SERVICE_MOCK = Mock(HttpRemoteService) {
        post("/api/report", _ as String) >> { List args ->
            HttpResponse httpResponseMock = Mock(HttpResponse)
            HttpEntity httpEntityMock = Mock(HttpEntity)

            RenderTemplateRequest req = GSON.fromJson(
                    new StringReader(args[1] as String),
                    RenderTemplateRequest.class
            )

            def templateId = req.template.name

            if (! templateId) {
                templateId = req.template.shortid
            }

            if (templateId.startsWith("code_")) {
                httpResponseMock.getStatusLine() >> { return new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), Integer.parseInt(templateId.substring(5)), "Test") }

            } else if (templateId.startsWith("ex_")) {
                String exType = templateId.substring(3)
                switch (exType) {
                    case "UnsupportedEncodingException": throw new UnsupportedEncodingException()
                    case "URISyntaxException": throw new URISyntaxException("test", "test")
                    case "JsReportException": throw new JsReportException()
                    case "IOException": throw new IOException()
                    default: throw new JsReportException()
                }

            } else if (templateId.startsWith("ic_")) {
                String exType = templateId.substring(3)
                Throwable e
                switch (exType) {
                    case "UnsupportedOperationException":
                        e = new UnsupportedOperationException()
                        break
                    case "IOException":
                        e = new IOException()
                        break
                    default: throw new JsReportException()
                }

                httpResponseMock.getStatusLine() >> { return new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "Test") }
                httpEntityMock.getContent() >> { throw e }

            } else {
                httpResponseMock.getStatusLine() >> { return new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "Test") }
                httpEntityMock.getContent() >> { return ReportingServiceImplSpec.getResourceAsStream("${templateId}.pdf") }

            }

            httpResponseMock.getEntity() >> { return httpEntityMock }

            return httpResponseMock
        }

        findHeader(_ as HttpResponse, HttpRemoteServiceImpl.HEADER_CONTENT_TYPE) >> { return CONTENT_TYPE_HEADER }
        findAndParseHeader(_ as HttpResponse , HttpRemoteServiceImpl.HEADER_FILE_EXTENSION) >> { return "" }
    }

    //
    // Asserts
    //

    private static void assertReport(Report report, String requiredText) {
        assert report != null
        assert report.content != null
        assert report.fileExtension == ""
        assert report.contentType == CONTENT_TYPE_HEADER
        assert report.getPermanentLink() == null

        def load = PDDocument.load(report.content)
        assert requiredText == PDF_TEXT_STRIPPER.getText(load)
        load.close()
    }

}
