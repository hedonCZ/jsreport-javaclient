package net.jsreport.java

import net.jsreport.java.dto.CreateTemplateRequest
import net.jsreport.java.dto.RenderTemplateRequest
import net.jsreport.java.entity.Report
import net.jsreport.java.entity.Template
import net.jsreport.java.service.*
import org.apache.log4j.Logger
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream
import org.apache.pdfbox.pdfparser.PDFParser
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.junit.experimental.categories.Category
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@Category(IntegrationTest.class)
class ReportingServiceITSpec extends Specification {

    private static final Logger log = Logger.getLogger(ReportingServiceITSpec.class)

    private static final String PDF_TEXT_CONTENT = "Simple test of template call!"

    @Shared
    private HttpRemoteService httpRemoteService = new HttpRemoteServiceImpl("http://jsreport:9080")

    @Shared
    private ReportingService reportingService = new ReportingServiceImpl(httpRemoteService)

    @Shared
    private TemplateService templateService = new TemplateServiceImpl(httpRemoteService)

    @Shared
    Template testingTemplate

    @Shared
    CreateTemplateRequest templateRequest =
            new CreateTemplateRequest(
                    name: "test-api",
                    content: "<h1>${PDF_TEXT_CONTENT}</h1>",
                    recipe: "chrome-pdf",
                    engine: "handlebars"
            )


    def setupSpec() {
        testingTemplate = templateService.putTemplate(templateRequest)
    }

    def cleanupSpec() {
        templateService.removeTemplate(testingTemplate._id)
    }

    @Unroll
    def "test render template"() {
        when:

        Report report = reportingService.render(renderRequest)

        then:

        assertReport(
                report,
                PDF_TEXT_CONTENT
        )

        where:

        renderRequest << [
                new RenderTemplateRequest(template: new Template(name: "/test-api")),
                new RenderTemplateRequest(template: new Template(shortid: testingTemplate.shortid))
        ]
    }

    void assertReport(Report report, String pdfContent) {
        assert report != null : "Rendered report is null!"
        assert report.getContentType() != null : "Rendered report do not have set Content type header!"
        assert report.getContentType().getValue() == "application/pdf"

        if (pdfContent != null) {
            assert getTextFromPdf(report.getContent()).trim() == pdfContent.trim()
        }
    }

    def getTextFromPdf(InputStream pdfSrc) {
        def parser = new PDFParser(new RandomAccessBufferedFileInputStream(pdfSrc))
        parser.parse();
        def cosDoc = parser.getDocument();
        def pdfStripper = new PDFTextStripper();
        def pdDoc = new PDDocument(cosDoc);
        def parsedText = pdfStripper.getText(pdDoc);

        log.debug("Find this text in PDF: ${parsedText}")

        return parsedText
    }
}
