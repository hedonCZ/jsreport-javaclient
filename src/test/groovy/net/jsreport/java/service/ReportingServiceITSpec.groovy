package net.jsreport.java.service

import com.google.gson.Gson
import net.jsreport.java.JsReportException
import net.jsreport.java.dto.CreateTemplateRequest
import net.jsreport.java.dto.RenderTemplateRequest
import net.jsreport.java.entity.Report
import net.jsreport.java.entity.Template
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class ReportingServiceITSpec extends Specification {

    private static final Gson GSON = new Gson()
    private static final PDFTextStripper PDF_TEXT_STRIPPER = new PDFTextStripper()
    private static final String PDF_TEXT_CONTENT = "Simple test of template call!"
    private static final String PDF_TEXT_DATA_CONTENT = "Hello jsreport!"

    @Shared
    private HttpRemoteService httpRemoteService = new HttpRemoteServiceImpl("http://localhost:9080")

    @Shared
    private ReportingService reportingService = new ReportingServiceImpl(httpRemoteService)

    @Shared
    private TemplateService templateService = new TemplateServiceImpl(httpRemoteService)

    @Shared
    CreateTemplateRequest templateRequest =
            new CreateTemplateRequest(
                    name: "test-api",
                    content: "<h1>${PDF_TEXT_CONTENT}</h1>",
                    recipe: "chrome-pdf",
                    engine: "handlebars"
            )

    @Shared
    CreateTemplateRequest templateDataRequest =
            new CreateTemplateRequest(
                    name: "test-data",
                    content: "<h1>Hello {{user}}!</h1>",
                    recipe: "chrome-pdf",
                    engine: "handlebars"
            )

    @Shared
    Template testingTemplate

    @Shared
    Template testingDataTemplate

    def setupSpec() {
        try {
            testingTemplate = templateService.putTemplate(templateRequest)
        } catch(Throwable e) {
            println "Sink exception: ${e.getMessage()} on creating: ${templateRequest}"
        }

        try {
            testingDataTemplate = templateService.putTemplate(templateDataRequest)
        } catch(Throwable e) {
            println "Sink exception: ${e.getMessage()} on creating: ${templateDataRequest}"
        }
    }

    def cleanupSpec() {
        try {
            templateService.removeTemplate(testingTemplate._id)
        } catch(Throwable e) {
            println "Sink exception: ${e.getMessage()} on removing: ${testingTemplate}"
        }

        try {
            templateService.removeTemplate(testingDataTemplate._id)
        } catch(Throwable e) {
            println "Sink exception: ${e.getMessage()} on removing: ${templateDataRequest}"
        }
    }

    @Unroll
    def testRenderByRequest_OK() {
        when:

        RenderTemplateRequest renderRequest = new RenderTemplateRequest()
        renderRequest.template = template
        renderRequest.data = data

        Report report = reportingService.render(renderRequest)

        then:

        assertReport(report, text)

        where:

        template                                            | text                  | data
        new Template(name: "test-api")                      | PDF_TEXT_CONTENT      | null
        new Template(shortid: testingTemplate.shortid)      | PDF_TEXT_CONTENT      | null
        new Template(name: "test-data")                     | PDF_TEXT_DATA_CONTENT | [ "user" : "jsreport" ]
        new Template(shortid: testingDataTemplate.shortid)  | PDF_TEXT_DATA_CONTENT | [ "user" : "jsreport" ]

    }

    @Unroll
    def testRenderByRequest_Errors() {
        setup:

        RenderTemplateRequest renderRequest = new RenderTemplateRequest()
        renderRequest.template = template
        renderRequest.data = data

        when:

        Throwable caught = null
        try {
            reportingService.render(renderRequest)
        } catch (Throwable e) {
            caught = e
        }

        then:

        assert caught != null
        assert caught.class == exceptionClass
        assert caught.getMessage() == text

        where:

        template                                | exceptionClass            | text                              | data
        new Template(name: "not-exist")         | JsReportException.class   | "Invalid status code (404) !!!"   | null
        new Template(shortid: "not-exist")      | JsReportException.class   | "Invalid status code (404) !!!"   | null
        new Template()                          | JsReportException.class   | "Invalid status code (400) !!!"   | null
        new Template(name: "not-exist")         | JsReportException.class   | "Invalid status code (404) !!!"   | [ "some" : "data" ]
        new Template(shortid: "not-exist")      | JsReportException.class   | "Invalid status code (404) !!!"   | [ "some" : "data" ]
        new Template()                          | JsReportException.class   | "Invalid status code (400) !!!"   | [ "some" : "data" ]

    }

    @Unroll
    def testOtherRenderMethods_OK() {
        when:

        Report reportByStringObject = reportingService.render(template.shortid, data)
        Report reportByStringString = reportingService.render(template.shortid, GSON.toJson(data))

        then:

        assertReport(reportByStringObject, text)
        assertReport(reportByStringString, text)

        where:

        template                                            | text                  | data
        new Template(shortid: testingTemplate.shortid)      | PDF_TEXT_CONTENT      | null
        new Template(shortid: testingDataTemplate.shortid)  | PDF_TEXT_DATA_CONTENT | [ "user" : "jsreport" ]
    }

    @Unroll
    def testRenderByOtherMethods_Error() {
        when:

        Throwable caught = null
        try {
            reportingService.render(shortId, data)
        } catch (Throwable e) {
            caught = e
        }

        then:

        assert caught != null
        assert caught.class == exceptionClass
        assert caught.getMessage() == text

        where:

        shortId     | exceptionClass            | text                              | data
        "not-exist" | JsReportException.class   | "Invalid status code (404) !!!"   | null
        null        | JsReportException.class   | "Invalid status code (400) !!!"   | null
        "not-exist" | JsReportException.class   | "Invalid status code (404) !!!"   | "{ \"some\" : \"data\"}"
        null        | JsReportException.class   | "Invalid status code (404) !!!"   | "{ \"some\" : \"data\"}"
        "not-exist" | JsReportException.class   | "Invalid status code (400) !!!"   | "{ \"invalid\" : \"data\". }"
        null        | JsReportException.class   | "Invalid status code (400) !!!"   | "{ \"invalid\" : \"data\". }"
        "not-exist" | JsReportException.class   | "Invalid status code (404) !!!"   | [ "some" : "data" ]
        null        | JsReportException.class   | "Invalid status code (400) !!!"   | [ "some" : "data" ]
        "not-exist" | JsReportException.class   | "Invalid status code (404) !!!"   | [ "some", "data" ]
        null        | JsReportException.class   | "Invalid status code (400) !!!"   | [ "some", "data" ]
    }

    @Unroll
    def testRenderByStringObject_Error() {
        when:

        Throwable caught = null
        try {
            reportingService.render(shortId, data)
        } catch (Throwable e) {
            caught = e
        }

        then:

        assert caught != null
        assert caught.class == exceptionClass
        assert caught.getMessage() == text

        where:

        shortId     | exceptionClass            | text                              | data
        "not-exist" | JsReportException.class   | "Invalid status code (404) !!!"   | null
        null        | JsReportException.class   | "Invalid status code (400) !!!"   | null
        "not-exist" | JsReportException.class   | "Invalid status code (404) !!!"   | "{ \"some\" : \"data\"}"
        null        | JsReportException.class   | "Invalid status code (404) !!!"   | "{ \"some\" : \"data\"}"
        "not-exist" | JsReportException.class   | "Invalid status code (400) !!!"   | "{ \"invalid\" : \"data\". }"
        null        | JsReportException.class   | "Invalid status code (400) !!!"   | "{ \"invalid\" : \"data\". }"

    }

    void assertReport(Report report, String pdfContent) {
        assert report != null : "Rendered report is null!"
        assert report.getContentType() != null : "Rendered report do not have set Content type header!"
        assert report.getContentType().getValue() == "application/pdf"

        if (pdfContent != null) {
            assert PDF_TEXT_STRIPPER.getText(PDDocument.load(report.getContent())).trim() == pdfContent.trim()
        }
    }
}
