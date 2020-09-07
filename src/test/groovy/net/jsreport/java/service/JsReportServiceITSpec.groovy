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

class JsReportServiceITSpec extends Specification {

    private static final PDFTextStripper PDF_TEXT_STRIPPER = new PDFTextStripper()
    private static final String PDF_TEXT_CONTENT = "Simple test of template call!"
    private static final String PDF_TEXT_DATA_CONTENT = "Hello jsreport!"

    @Shared
    private JsReportServiceImpl jsReportService = new JsReportServiceImpl("http://localhost:9080")

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
    CreateTemplateRequest templateRequestForTemplates =
            new CreateTemplateRequest(
                    name: "test-api-template",
                    content: "<h1>${PDF_TEXT_CONTENT}</h1>",
                    recipe: "chrome-pdf",
                    engine: "handlebars"
            )


    @Shared
    Template testingTemplate

    @Shared
    Template testingDataTemplate

    def setupSpec() {
        try {
            testingTemplate = jsReportService.putTemplate(templateRequest)
        } catch(Throwable e) {
            println "Sink exception: ${e.getMessage()} on creating: ${templateRequest}"
        }

        try {
            testingDataTemplate = jsReportService.putTemplate(templateDataRequest)
        } catch(Throwable e) {
            println "Sink exception: ${e.getMessage()} on creating: ${templateDataRequest}"
        }
    }

    def cleanupSpec() {
        try {
            jsReportService.removeTemplate(testingTemplate._id)
        } catch(Throwable e) {
            println "Sink exception: ${e.getMessage()} on removing: ${testingTemplate}"
        }

        try {
            jsReportService.removeTemplate(testingDataTemplate._id)
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

        Report report = jsReportService.render(renderRequest)

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
            jsReportService.render(renderRequest)
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
    
/*
    @Unroll
    def testOtherRenderMethods_OK() {
        when:

        Report reportByStringObject = jsReportService.render(template.shortid, data)
        Report reportByStringString = jsReportService.render(template.shortid, GSON.toJson(data))

        then:

        assertReport(reportByStringObject, text)
        assertReport(reportByStringString, text)

        where:

        template                                            | text                  | data
        new Template(shortid: testingTemplate.shortid)      | PDF_TEXT_CONTENT      | null
        new Template(shortid: testingDataTemplate.shortid)  | PDF_TEXT_DATA_CONTENT | [ "user" : "jsreport" ]
    }
*/

/*
    @Unroll
    def testRenderByOtherMethods_Error() {
        when:

        Throwable caught = null
        try {
            jsReportService.render(shortId, data)
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
*/

/*
    @Unroll
    def testRenderByStringObject_Error() {
        when:

        Throwable caught = null
        try {
            jsReportService.render(shortId, data)
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
*/

    def testPutAndRemoveTemplate_OK() {
        when:

        Template template1 = jsReportService.putTemplate(templateRequestForTemplates)
        jsReportService.removeTemplate(template1.get_id())
        Template template2 = jsReportService.putTemplate(templateRequestForTemplates)

        try {
            Template temp = jsReportService.putTemplate(templateRequestForTemplates)
            assert false
        } catch(JsReportException e) {
            println "Duplication insert fails correctly. Get exception: ${e}"
        }

        then:

        assertTemplate(template1, PDF_TEXT_CONTENT)
        assertTemplate(template2, PDF_TEXT_CONTENT)
        assert template2.shortid != template1.shortid

        cleanup:
        // removing not existing do not matter
        jsReportService.removeTemplate(template1.get_id())
        jsReportService.removeTemplate(template2.get_id())

    }

    @Unroll
    def testPutTemplate_Error() {
        when:

        Throwable caught = null
        CreateTemplateRequest createTemplateRequest = new CreateTemplateRequest()

        try {
            jsReportService.putTemplate(createTemplateRequest)
        } catch (Throwable e) {
            caught = e
        }

        then:

        assert caught != null
        assert caught.class == JsReportException.class
        assert caught.getMessage() == text

        where:

        request                                                                                                             | text
        new CreateTemplateRequest()                                                                                         | "Invalid status code (500) !!!"
        new CreateTemplateRequest(name: "invalid-test")                                                                     | "Invalid status code (500) !!!"
        new CreateTemplateRequest(content: "invalid-test")                                                                  | "Invalid status code (500) !!!"
        new CreateTemplateRequest(recipe: "invalid-test")                                                                   | "Invalid status code (500) !!!"
        new CreateTemplateRequest(engine: "invalid-test")                                                                   | "Invalid status code (500) !!!"
        new CreateTemplateRequest(name: "invalid-test", recipe: "invalid")                                                  | "Invalid status code (500) !!!"
        new CreateTemplateRequest(name: "invalid-test", content: "invalid-test", recipe: "invalid")                         | "Invalid status code (500) !!!"
        new CreateTemplateRequest(name: "invalid-test", engine: "invalid")                                                  | "Invalid status code (500) !!!"
        new CreateTemplateRequest(name: "invalid-test", content: "invalid-test",  engine: "invalid")                        | "Invalid status code (500) !!!"
        new CreateTemplateRequest(name: "invalid-test", content: "invalid-test",  engine: "invalid", recipe: "invalid")     | "Invalid status code (500) !!!"
    }

    def testRemoveTemplate_Error() {
        when:

        Throwable caught = null
        try {
            jsReportService.removeTemplate("invalid")
        } catch(Throwable e) {
            caught = e
        }

        then:

        assert caught == null
    }



    private static void assertTemplate(Template template, String text) {
        assert template != null
        assert template._id != null
        assert template.shortid != null
        assert template.getContent().contains(text)
    }

    static void assertReport(Report report, String pdfContent) {
        assert report != null : "Rendered report is null!"
        assert report.getContentType() != null : "Rendered report do not have set Content type header!"
        assert report.getContentType() == "application/pdf"

        if (pdfContent != null) {
            assert PDF_TEXT_STRIPPER.getText(PDDocument.load(report.getContent())).trim() == pdfContent.trim()
        }
    }


}
