package net.jsreport.java.service

import com.google.gson.Gson
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
    private HttpRemoteService httpRemoteService = new HttpRemoteServiceImpl("http://jsreport:9080")

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
        testingTemplate = templateService.putTemplate(templateRequest)
        testingDataTemplate = templateService.putTemplate(templateDataRequest)
    }

    def cleanupSpec() {
        templateService.removeTemplate(testingTemplate._id)
        templateService.removeTemplate(testingDataTemplate._id)
    }

    @Unroll
    def testRenderByRequest() {
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
    def testOtherRenderMethods() {
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

    void assertReport(Report report, String pdfContent) {
        assert report != null : "Rendered report is null!"
        assert report.getContentType() != null : "Rendered report do not have set Content type header!"
        assert report.getContentType().getValue() == "application/pdf"

        if (pdfContent != null) {
            assert PDF_TEXT_STRIPPER.getText(PDDocument.load(report.getContent())).trim() == pdfContent.trim()
        }
    }
}
