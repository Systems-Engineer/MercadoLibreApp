package com.brahian.mercadolibreapp.dispatcher

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.io.InputStreamReader
import java.lang.Exception
import java.util.concurrent.TimeUnit

class MockServerDispatcher {
    /**
     * Return ok response from mock server
     */
    internal inner class RequestDispatcher(private val rules : List<DispatcherRule>) : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            val rule = rules.find { it.path == request.path }
            return try {
                rule?.let {
                    MockResponse().setResponseCode(rule.code).setBody(getJsonContent(rule.resource))
                } ?: run {
                    MockResponse().setResponseCode(400)
                }
            } catch (ex : Exception) {
                MockResponse().setResponseCode(400)
            }.setBodyDelay(1, TimeUnit.SECONDS)
        }
    }

    /**
     * Return error response from mock server
     */
    internal inner class ErrorDispatcher : Dispatcher() {

        override fun dispatch(request: RecordedRequest): MockResponse {
            return MockResponse().setResponseCode(400)
        }
    }

    private fun getJsonContent(fileName: String): String {
        return InputStreamReader(this.javaClass.classLoader!!.getResourceAsStream(fileName)).use { it.readText() }
    }
}

data class DispatcherRule(val path : String, val code : Int = 200, val resource : String)