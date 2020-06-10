package net.jsreport.java


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
    private ReportingService service
    @Shared
    private TemplateService crudService
    @Shared
    private Template testingTemplate

    def setupSpec() {
        crudService = new TemplateServiceImpl("http://jsreport:9080")
        service = new ReportingServiceImpl("http://jsreport:9080")

        TemplateRequest templateRequest = new TemplateRequest()
        templateRequest.setName("test-api")
        templateRequest.setContent("<h1>${PDF_TEXT_CONTENT}</h1>")
        templateRequest.setRecipe("chrome-pdf")
        templateRequest.setEngine("handlebars")

        testingTemplate = crudService.putTemplate(templateRequest)
    }

    def cleanupSpec() {
        crudService.removeTemplate(testingTemplate._id)
    }

    @Unroll
    def "render - using request"() {
        when:

        Report report = service.render(renderRequest)

        then:

        assertReport(
                report,
                PDF_TEXT_CONTENT
        )

        where:

        renderRequest << [
                new RenderRequest(template: new Template(name: "/test-api")),
                new RenderRequest(template: new Template(shortid: testingTemplate.shortid))
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
