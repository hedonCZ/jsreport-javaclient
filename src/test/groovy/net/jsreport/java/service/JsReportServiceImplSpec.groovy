package net.jsreport.java.service

import net.jsreport.java.JsReportException

import net.jsreport.java.dto.Template
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.BufferedSource
import retrofit2.Call
import retrofit2.Response
import spock.lang.Specification

class JsReportServiceImplSpec extends Specification {

    static class FakeResponseBody extends ResponseBody {
        @Override
        MediaType contentType() {
            return MediaType.get("application/json")
        }

        @Override
        long contentLength() {
            return 0
        }

        @Override
        BufferedSource source() {
            return null
        }
    }

    def testPutTemplateSync_OK() {
        when:

        JsReportServiceImpl jsReportService = prepareServiceWithResponse(Response.success(new Template(name: "test")))
        def template = jsReportService.putTemplate(new Template())

        then:

        assert template != null

    }

    def testPutTemplateSync_NOT_OK() {
        when:

        JsReportServiceImpl jsReportService = prepareServiceWithResponse(Response.error(400, new FakeResponseBody()))
        jsReportService.putTemplate(new Template())

        then:

        thrown JsReportException

    }

    def testRemoveTemplate_OK () {
        when:

        JsReportServiceImpl jsReportService = prepareServiceWithResponse(Response.success(200, null))
        jsReportService.removeTemplate("test")

        then:
        {}

    }

    def testRemoveTemplate_NOT_OK () {
        when:

        JsReportServiceImpl jsReportService = prepareServiceWithResponse(Response.error(400, new FakeResponseBody()))
        jsReportService.removeTemplate("test")

        then:

        thrown JsReportException
    }

    private JsReportServiceImpl prepareServiceWithResponse(response) {
        JsReportServiceImpl jsReportService = new JsReportServiceImpl("http://foo")
        jsReportService.jsreportRetrofitService = Mock(JsReportRetrofitService) {
            removeTemplate(_) >> { String id ->
                Call<Void> mockCall = Mock(Call) {
                    execute() >>  { return response }
                }

                return mockCall
            }

            putTemplate(_) >> { Template request ->
                Call<Template> mockCall = Mock(Call) {
                    execute() >> { return response }
                }

                return mockCall
            }
        }

        return jsReportService
    }
}
