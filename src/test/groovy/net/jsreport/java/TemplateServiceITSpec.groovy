package net.jsreport.java

import net.jsreport.java.entity.Template
import net.jsreport.java.entity.TemplateRequest
import net.jsreport.java.service.TemplateService
import net.jsreport.java.service.TemplateServiceImpl
import org.apache.log4j.Logger
import org.junit.experimental.categories.Category
import spock.lang.Specification

@Category(IntegrationTest.class)
class TemplateServiceITSpec extends Specification {

    private static final Logger log = Logger.getLogger(TemplateServiceITSpec.class)

    private static final String PDF_TEXT_CONTENT = "Simple test of template call!"

    private TemplateService service
    private Template template1
    private Template template2
    private Template template3

    void setup() {
        service = new TemplateServiceImpl("http://jsreport:9080")

    }

    def "test template crud"() {
        when:
        TemplateRequest templateRequest = new TemplateRequest()
        templateRequest.setName("test-api")
        templateRequest.setContent("<h1>${PDF_TEXT_CONTENT}</h1>")
        templateRequest.setRecipe("chrome-pdf")
        templateRequest.setEngine("handlebars")

        template1 = service.putTemplate(templateRequest)
        service.removeTemplate(template1.get_id())
        template2 = service.putTemplate(templateRequest)

        try {
            template3 = service.putTemplate(templateRequest)
            assert false
        } catch(JsReportException e) {
            println "Get exception: ${e}"
        }

        service.removeTemplate(template2.get_id())
        service.removeTemplate(template2.get_id())

        then:

        assert template1 != null
        assert template1.getContent().contains(PDF_TEXT_CONTENT)
        assert template2 != null
        assert template2.content.contains(PDF_TEXT_CONTENT)
        assert template2.shortid != template1.shortid
        assert template3 == null

    }

    def after() {
        service.removeTemplate(template1.get_id())
        service.removeTemplate(template2.get_id())
        service.removeTemplate(template3.get_id())
    }
}
