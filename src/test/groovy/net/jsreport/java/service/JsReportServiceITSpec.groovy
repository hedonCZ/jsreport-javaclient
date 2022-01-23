package net.jsreport.java.service

import com.google.gson.Gson
import net.jsreport.java.JsReportException
import net.jsreport.java.dto.*
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Timeout
import spock.lang.Unroll

import java.util.concurrent.ExecutionException
import java.util.concurrent.Future

class JsReportServiceITSpec extends Specification {

    private static final PDFTextStripper PDF_TEXT_STRIPPER = new PDFTextStripper()
    private static final String PDF_TEXT_CONTENT = "Simple test of template call!"
    private static final String PDF_TEXT_DATA_CONTENT = "Hello jsreport!"

    @Shared
    private JsReportServiceImpl jsReportService = new JsReportServiceImpl("http://localhost:9080")

    @Shared
    private JsReportServiceImpl jsReportServiceAuth = new JsReportServiceImpl("http://localhost:10080", "admin", "xxx")

    @Shared
    Template anonymousTemplate =
            new Template(
                    content: "<h1>Hello {{user}}!</h1>",
                    recipe: Recipe.CHROME_PDF,
                    engine: Engine.HANDLEBARS
            )

    @Shared
    Template persistedTemplate =
            new Template(
                    name: "myTemplate",
                    shortid: "myShortid",
                    content: "<h1>Hello {{user}}!</h1>",
                    recipe: Recipe.CHROME_PDF,
                    engine: Engine.HANDLEBARS
            )

    @Unroll
    def testRenderRequest_OK() {

        setup:

        def createdTemplate = service.putTemplate(persistedTemplate)

        when:

        RenderRequest renderRequest = new RenderRequest()
        renderRequest.template = template
        renderRequest.data = [ "user" : "jsreport" ]

        Report report = service.render(renderRequest)

        then:

        assertReport(report, PDF_TEXT_DATA_CONTENT)

        cleanup:

        if (createdTemplate) {
            service.removeTemplate(createdTemplate._id)
        }

        where:

        service             | template
        jsReportService     | anonymousTemplate
        jsReportService     | new Template(name: persistedTemplate.name)
        jsReportServiceAuth | anonymousTemplate
        jsReportService     | new Template(shortid: persistedTemplate.shortid)
    }

    @Unroll
    def testRenderMap_OK() {

        setup:

        def createdTemplate = service.putTemplate(persistedTemplate)

        when:

        Report report = service.render(
                [ "template":
                          [ "name": createdTemplate.name ],
                  "data":
                          [ "user" : "jsreport" ]
                ]
        )

        then:

        assertReport(report, PDF_TEXT_DATA_CONTENT)

        cleanup:

        if (createdTemplate) {
            service.removeTemplate(createdTemplate._id)
        }

        where:

        service << [
                jsReportService,
                jsReportServiceAuth
        ]
    }

    @Unroll
    def testRenderByJson() {
        setup:

        def createdTemplate = service.putTemplate(persistedTemplate)

        when:

        String jsonString = "{ \"template\" : { \"name\" : \"${persistedTemplate.name}\" }, \"data\" : { \"user\" : \"jsreport\" } }"
        Map<String, Object> serializedJson = new Gson().fromJson(jsonString, Map<String, Object>.class)
        Report report = service.render(serializedJson)

        then:

        assertReport(report, PDF_TEXT_DATA_CONTENT)

        cleanup:

        if (createdTemplate) {
            service.removeTemplate(createdTemplate.get_id())
        }

        where:

        service << [
                jsReportService,
                jsReportServiceAuth
        ]
    }

    @Unroll
    def testRenderByNameRequest_OK() {

        setup:

        def createdTemplate = service.putTemplate(persistedTemplate)

        when:

        Report report = service.render(persistedTemplate.name, [ "user" : "jsreport" ])

        then:

        assertReport(report, PDF_TEXT_DATA_CONTENT)

        cleanup:

        if (createdTemplate) {
            service.removeTemplate(createdTemplate._id)
        }

        where:

        service << [
                jsReportService,
                jsReportServiceAuth
        ]
    }

    @Unroll
    @Timeout(15)
    def testRenderByRequestAsync_OK() {
        setup:

        def testingTemplate = service.putTemplate(persistedTemplate)

        when:

        RenderRequest renderRequest = new RenderRequest()
        renderRequest.template = template
        renderRequest.data = [ "user" : "jsreport" ]

        Future<Report> futureReport = service.renderAsync(renderRequest)

        then:

        assertReport(futureReport.get(), PDF_TEXT_DATA_CONTENT)

        cleanup:

        if (testingTemplate) {
            service.removeTemplate(testingTemplate._id)
        }

        where:

        service             | template
        jsReportService     | anonymousTemplate
        jsReportService     | new Template(name: persistedTemplate.name)
        jsReportService     | new Template(shortid: persistedTemplate.shortid)
        jsReportServiceAuth | anonymousTemplate
        jsReportServiceAuth | new Template(name: persistedTemplate.name)
        jsReportServiceAuth | new Template(shortid: persistedTemplate.shortid)

    }


    @Unroll
    @Timeout(15)
    def testRenderByRequestAsync_Errors() {
        setup:

        RenderRequest renderRequest = new RenderRequest()
        renderRequest.template = template
        renderRequest.data = data

        when:

        Future<Report> reportFuture= service.renderAsync(renderRequest)

        then:

        def caught = null
        try {
            reportFuture.get()
        } catch(ExecutionException e) {
            caught = e.getCause()
        }

        assert caught != null
        assert caught.class == exceptionClass
        assert caught.getMessage() == text

        where:

        service             |template                                | exceptionClass            | text                              | data
        jsReportService     |new Template(name: "not-exist")         | JsReportException.class   | "Invalid status code (404) !!!"   | null
        jsReportService     |new Template(shortid: "not-exist")      | JsReportException.class   | "Invalid status code (404) !!!"   | null
        jsReportService     |new Template()                          | JsReportException.class   | "Invalid status code (400) !!!"   | null
        jsReportService     |new Template(name: "not-exist")         | JsReportException.class   | "Invalid status code (404) !!!"   | [ "some" : "data" ]
        jsReportService     |new Template(shortid: "not-exist")      | JsReportException.class   | "Invalid status code (404) !!!"   | [ "some" : "data" ]
        jsReportService     |new Template()                          | JsReportException.class   | "Invalid status code (400) !!!"   | [ "some" : "data" ]
        jsReportServiceAuth |new Template(name: "not-exist")         | JsReportException.class   | "Invalid status code (404) !!!"   | null
        jsReportServiceAuth |new Template(shortid: "not-exist")      | JsReportException.class   | "Invalid status code (404) !!!"   | null
        jsReportServiceAuth |new Template()                          | JsReportException.class   | "Invalid status code (400) !!!"   | null
        jsReportServiceAuth |new Template(name: "not-exist")         | JsReportException.class   | "Invalid status code (404) !!!"   | [ "some" : "data" ]
        jsReportServiceAuth |new Template(shortid: "not-exist")      | JsReportException.class   | "Invalid status code (404) !!!"   | [ "some" : "data" ]
        jsReportServiceAuth |new Template()                          | JsReportException.class   | "Invalid status code (400) !!!"   | [ "some" : "data" ]

    }


    @Unroll
    def testRenderByRequest_Errors() {
        setup:

        RenderRequest renderRequest = new RenderRequest()
        renderRequest.template = template
        renderRequest.data = data

        when:

        Throwable caught = null
        try {
            service.render(renderRequest)
        } catch (Throwable e) {
            caught = e
        }

        then:

        assert caught != null
        assert caught.class == exceptionClass
        assert caught.getMessage() == text

        where:

        service             |template                                | exceptionClass            | text                              | data
        jsReportService     |new Template(name: "not-exist")         | JsReportException.class   | "Invalid status code (404) !!!"   | null
        jsReportService     |new Template(shortid: "not-exist")      | JsReportException.class   | "Invalid status code (404) !!!"   | null
        jsReportService     |new Template()                          | JsReportException.class   | "Invalid status code (400) !!!"   | null
        jsReportService     |new Template(name: "not-exist")         | JsReportException.class   | "Invalid status code (404) !!!"   | [ "some" : "data" ]
        jsReportService     |new Template(shortid: "not-exist")      | JsReportException.class   | "Invalid status code (404) !!!"   | [ "some" : "data" ]
        jsReportService     |new Template()                          | JsReportException.class   | "Invalid status code (400) !!!"   | [ "some" : "data" ]
        jsReportServiceAuth |new Template(name: "not-exist")         | JsReportException.class   | "Invalid status code (404) !!!"   | null
        jsReportServiceAuth |new Template(shortid: "not-exist")      | JsReportException.class   | "Invalid status code (404) !!!"   | null
        jsReportServiceAuth |new Template()                          | JsReportException.class   | "Invalid status code (400) !!!"   | null
        jsReportServiceAuth |new Template(name: "not-exist")         | JsReportException.class   | "Invalid status code (404) !!!"   | [ "some" : "data" ]
        jsReportServiceAuth |new Template(shortid: "not-exist")      | JsReportException.class   | "Invalid status code (404) !!!"   | [ "some" : "data" ]
        jsReportServiceAuth |new Template()                          | JsReportException.class   | "Invalid status code (400) !!!"   | [ "some" : "data" ]

    }

    @Unroll
    def testPutAndRemoveTemplate_OK() {
        when:

        Template template1 = service.putTemplate(persistedTemplate)
        service.removeTemplate(template1.get_id())
        Template template2 = service.putTemplate(persistedTemplate)

        try {
            Template temp = service.putTemplate(persistedTemplate)
            assert false
        } catch(JsReportException e) {
            println "Duplication insert fails correctly. Get exception: ${e}"
        }

        then:

        assertTemplate(template2, persistedTemplate.content)

        cleanup:
        // removing not existing do not matter
        service.removeTemplate(template1.get_id())
        service.removeTemplate(template2.get_id())

        where:

        service << [
            jsReportService,
            jsReportServiceAuth
        ]
    }

    @Unroll
    def testPutTemplate_Error() {
        when:

        Throwable caught = null
        Template createTemplateRequest = new Template()

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

        service             | request                                                                                                        | text
        jsReportService     | new Template()                                                                                    | "Invalid status code (500) !!!"
        jsReportService     | new Template(name: "invalid-test")                                                                | "Invalid status code (500) !!!"
        jsReportService     | new Template(content: "invalid-test")                                                             | "Invalid status code (500) !!!"
        jsReportServiceAuth | new Template()                                                                                    | "Invalid status code (500) !!!"
        jsReportServiceAuth | new Template(name: "invalid-test")                                                                | "Invalid status code (500) !!!"
        jsReportServiceAuth | new Template(content: "invalid-test")                                                             | "Invalid status code (500) !!!"
    }

    @Unroll
    def testRemoveTemplate_Error() {
        when:

        Throwable caught = null
        try {
            service.removeTemplate("invalid")
        } catch(Throwable e) {
            caught = e
        }

        then:

        assert caught == null

        where:

        service << [
                jsReportService,
                jsReportServiceAuth
        ]
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
