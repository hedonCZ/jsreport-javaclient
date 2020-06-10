package net.jsreport.java

import net.jsreport.java.entity.Template
import net.jsreport.java.dto.CreateTemplateRequest
import net.jsreport.java.service.HttpRemoteService
import net.jsreport.java.service.HttpRemoteServiceImpl
import net.jsreport.java.service.TemplateService
import net.jsreport.java.service.TemplateServiceImpl
import org.apache.log4j.Logger
import org.junit.experimental.categories.Category
import spock.lang.Shared
import spock.lang.Specification

@Category(IntegrationTest.class)
class TemplateServiceITSpec extends Specification {

    private static final Logger log = Logger.getLogger(TemplateServiceITSpec.class)

    private static final String PDF_TEXT_CONTENT = "Simple test of template call!"

    @Shared
    private HttpRemoteService httpRemoteService = new HttpRemoteServiceImpl("http://jsreport:9080")

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

    def "test put & remove"() {
        when:

        Template template1 = templateService.putTemplate(templateRequest)
        templateService.removeTemplate(template1.get_id())
        Template template2 = templateService.putTemplate(templateRequest)

        try {
            Template temp = templateService.putTemplate(templateRequest)
            assert false
        } catch(JsReportException e) {
            println "Get exception: ${e}"
        }

        // removing not existing do not matter
        templateService.removeTemplate(template2.get_id())
        templateService.removeTemplate(template2.get_id())

        then:

        assert template1 != null
        assert template1.getContent().contains(PDF_TEXT_CONTENT)
        assert template2 != null
        assert template2.content.contains(PDF_TEXT_CONTENT)
        assert template2.shortid != template1.shortid

        cleanup:

        templateService.removeTemplate(template1.get_id())
        templateService.removeTemplate(template2.get_id())
    }

    def cleanup() {
    }
}
