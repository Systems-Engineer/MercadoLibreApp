package com.brahian.mercadolibreapp.dispatcher

import android.util.Log
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.io.InputStreamReader
import java.lang.Exception
import java.util.concurrent.TimeUnit

class MockServerDispatcher {

    private val jsonReader = JsonReader()

    companion object {
        val TAG = "TEST_" + this::class.java.name
    }

    /**
     * Dispatcher for successful requests
     */
    internal inner class RequestDispatcher(private val rules : List<DispatcherRule>) : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            val rule = rules.find { it.path == request.path }
            return try {
                rule?.let {
                    MockResponse().setResponseCode(rule.code).setBody(jsonReader.read(rule.resource))
                } ?: run {
                    MockResponse().setResponseCode(400)
                }
            } catch (ex : Exception) {
                MockResponse().setResponseCode(400)
            }.setBodyDelay(3, TimeUnit.SECONDS)
        }
    }

    /**
     * Dispatcher for error responses
     */
    internal inner class ErrorDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return MockResponse().setResponseCode(400).setHeadersDelay(1, TimeUnit.SECONDS)
        }
    }
}

data class DispatcherRule(val path : String, val code : Int = 200, val resource : String)

class JsonReader {
    fun read(fileName: String) : String {
        return try {
            InputStreamReader(this.javaClass.classLoader?.getResourceAsStream(fileName)).use { it.readText() }
        } catch (ex : Exception) {
            Log.d(MockServerDispatcher.TAG, "Exception thrown ($ex) - while trying to read file $fileName")
            ""
        }
    }
}