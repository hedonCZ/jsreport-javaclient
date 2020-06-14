package net.jsreport.java.service

import net.jsreport.java.JsReportException
import net.jsreport.java.dto.CreateTemplateRequest
import net.jsreport.java.entity.Template
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class TemplateServiceITSpec extends Specification {

    private static final String PDF_TEXT_CONTENT = "Simple test of template call!"

    @Shared
    CreateTemplateRequest templateRequest =
            new CreateTemplateRequest(
                    name: "test-api",
                    content: "<h1>${PDF_TEXT_CONTENT}</h1>",
                    recipe: "chrome-pdf",
                    engine: "handlebars"
            )

    def testPutAndRemove_OK () {
        setup:

        HttpRemoteService httpRemoteService = new HttpRemoteServiceImpl("http://jsreport:9080")
        TemplateService templateService = new TemplateServiceImpl(httpRemoteService)

        when:

        Template template1 = templateService.putTemplate(templateRequest)
        templateService.removeTemplate(template1.get_id())
        Template template2 = templateService.putTemplate(templateRequest)

        try {
            Template temp = templateService.putTemplate(templateRequest)
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
        templateService.removeTemplate(template1.get_id())
        templateService.removeTemplate(template2.get_id())

    }

    @Unroll
    def testPut_Error() {
        setup:

        HttpRemoteService httpRemoteService = new HttpRemoteServiceImpl("http://jsreport:9080")
        TemplateService templateService = new TemplateServiceImpl(httpRemoteService)

        when:

        Throwable caught = null
        CreateTemplateRequest createTemplateRequest = new CreateTemplateRequest()

        try {
            templateService.putTemplate(createTemplateRequest)
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


    @Unroll
    def testRemove_Error() {
        setup:

        HttpRemoteService httpRemoteService = new HttpRemoteServiceImpl("http://jsreport:9080")
        TemplateService templateService = new TemplateServiceImpl(httpRemoteService)

        when:

        Throwable caught = null
        try {
            templateService.removeTemplate(shortId)
        } catch(Throwable e) {
            caught = e
        }

        then:

        assert caught == null

        where:

        shortId << [
                null,
                "invalid"
        ]
    }


    //
    // Asserts
    //

    private static void assertTemplate(Template template, String text) {
        assert template != null
        assert template._id != null
        assert template.shortid != null
        assert template.getContent().contains(text)
    }

}
