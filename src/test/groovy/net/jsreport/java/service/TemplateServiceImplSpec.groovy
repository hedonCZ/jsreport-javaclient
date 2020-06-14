package net.jsreport.java.service

import com.google.gson.Gson
import net.jsreport.java.JsReportException
import net.jsreport.java.dto.CreateTemplateRequest
import net.jsreport.java.entity.Template
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.ProtocolVersion
import org.apache.http.message.BasicStatusLine
import spock.lang.Specification
import spock.lang.Unroll

class TemplateServiceImplSpec extends Specification {

    private static final Gson GSON = new Gson()

    @Unroll
    def testPutTemplate() {
        setup:

        TemplateServiceImpl templateService = new TemplateServiceImpl(HTTP_REMOTE_SERVICE_MOCK)

        when:

        CreateTemplateRequest createTemplateRequest = new CreateTemplateRequest()
        createTemplateRequest.name = name

        Template template
        try {
            template = templateService.putTemplate(createTemplateRequest)
        } catch(Throwable e) {
            if (name.startsWith("code_") || name == "invalidJson") return
            if (name.contains(e.class.simpleName)) return
            if (e.cause != null && name.contains(e.cause.class.simpleName)) return

            assert false : "Invalid exception"
        }

        then:

        assertTemplate(template)

        where:

        name << [
            "ok",
            "invalidJson",
            "ex_UnsupportedEncodingException",
            "ex_JsReportException",
            "code_500"
        ]
    }

    @Unroll
    def testRemoveTemplate() {
        setup:

        TemplateServiceImpl templateService = new TemplateServiceImpl(HTTP_REMOTE_SERVICE_MOCK)

        when:

        try {
            templateService.removeTemplate(name)
        } catch(Throwable e) {
            if (name.startsWith("code_") || name == "invalidJson") return
            if (name.contains(e.class.simpleName)) return
            if (e.cause != null && name.contains(e.cause.class.simpleName)) return

            assert false : "Invalid exception"
        }

        then:
        {}

        where:

        name << [
                "ok",
                "ex_URISyntaxException",
                "ex_JsReportException",
                "code_500"
        ]

    }


    //
    // Mocks
    //

    HttpRemoteService HTTP_REMOTE_SERVICE_MOCK = Mock(HttpRemoteService) {
        post("/odata/templates", _ as String) >> { List args ->

            CreateTemplateRequest req = GSON.fromJson(
                    new StringReader(args[1] as String),
                    CreateTemplateRequest.class
            )

            def controlString = req.name
            HttpResponse httpResponseMock = Mock(HttpResponse)
            if (controlString.startsWith("code_")) {
                httpResponseMock.getStatusLine() >> { return new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), Integer.parseInt(controlString.substring(5)), "Test") }

            } else if (controlString.startsWith("ex_")) {
                String exType = controlString.substring(3)
                switch (exType) {
                    case "UnsupportedEncodingException": throw new UnsupportedEncodingException()
                    case "URISyntaxException": throw new URISyntaxException("test", "test")
                    case "JsReportException": throw new JsReportException()
                    default: throw new JsReportException()
                }
            } else {
                String content
                if (controlString.startsWith("invalidJson")) {
                    content = "{"

                } else {
                    Template template = new Template()
                    template.name = req.name
                    template._id = UUID.randomUUID().toString()
                    template.shortid = UUID.randomUUID().toString().substring(0, 10)

                    content = GSON.toJson(template)
                }

                HttpEntity httpEntityMock = Mock(HttpEntity)
                httpResponseMock.getStatusLine() >> { return new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "Test") }
                httpEntityMock.getContent() >> { return new ByteArrayInputStream(content.bytes) }
                httpResponseMock.getEntity() >> { return httpEntityMock }

            }

            return httpResponseMock
        }

        delete(_ as String) >> { List args ->
            HttpResponse httpResponseMock = Mock(HttpResponse)

            String controlString = args[0]
            if (controlString.startsWith("code_")) {
                httpResponseMock.getStatusLine() >> { return new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), Integer.parseInt(controlString.substring(5)), "Test") }

            } else if (controlString.startsWith("ex_")) {
                String exType = controlString.substring(3)
                switch (exType) {
                    case "URISyntaxException": throw new URISyntaxException("test", "test")
                    case "JsReportException": throw new JsReportException()
                    default: throw new JsReportException()
                }
            } else {
                httpResponseMock.getStatusLine() >> { return new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "Test") }
                return httpResponseMock

            }
        }
    }

    //
    // Asserts
    //
    private static void assertTemplate(Template template) {
        assert template != null
        assert template.shortid != null
        assert template._id != null
    }
}
