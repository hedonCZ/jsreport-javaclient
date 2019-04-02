package net.jsreport.java


import org.apache.http.conn.socket.PlainConnectionSocketFactory
import org.apache.log4j.Logger
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream
import org.apache.pdfbox.pdfparser.PDFParser
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.junit.experimental.categories.Category
import spock.lang.Specification
import spock.lang.Unroll

@Category(IntegrationTest.class)
class ReportingServiceITSpec extends Specification {

    private static final Logger log = Logger.getLogger(ReportingServiceITSpec.class)

    private static final String PDF_TEXT_CONTENT = "Simple test of template call!"

    private ReportingService service

    def setup() {
        def serviceImpl = new ReportingServiceImpl()
        serviceImpl.getRegistryBuilder().register("http", PlainConnectionSocketFactory.getSocketFactory())
        serviceImpl.setServiceUri(new URI("http://localhost:9080/api/report"))
        service = serviceImpl
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
                new RenderRequest(template: new Template(name: "/test/test")),
                new RenderRequest(template: new Template(shortid: "r1m0hAFU4"))
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
