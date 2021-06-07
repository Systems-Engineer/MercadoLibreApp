package com.brahian.mercadolibreapp.util

import android.util.Log
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import org.hamcrest.Matcher
import java.time.Clock
import java.time.Duration

class Wait(private val view: Matcher<View>, private var timeout: Long = 10 * 1000L, private var pollingPeriod: Long = 100L) {

    companion object {
        val TAG = "TEST_" + this::class.java.canonicalName
        private val clock: Clock = Clock.systemDefaultZone()
        private val sleeper: Sleeper = Sleeper()
    }

    fun until(condition: ViewAssertion, timeout: Long = this.timeout, pollingPeriod: Long = this.pollingPeriod) {

        val end = clock.instant().plus(Duration.ofMillis(timeout))
        var lastException: Exception?
        var lastAssertionError: AssertionError?

        Log.d(TAG, "waiting for $timeout sec for condition $condition")
        while (true) {
            lastException = null
            lastAssertionError = null

            try {
                onView(view).check(condition)

                // Clear the last exception; if another retry or timeout exception would
                // be caused by a false or null value, the last exception is not the
                // cause of the timeout.
                lastException = null

                return
            } catch (e: Exception) {
                lastException = e
            } catch (ae: AssertionError) {
                lastAssertionError = ae
            }

            // Check the timeout after evaluating the function to ensure conditions with a zero timeout can succeed.
            if (end.isBefore(clock.instant())) {
                val timeoutMessage =
                    "Expected condition failed: waiting for $condition (tried for $timeout second(s) with $pollingPeriod milliseconds interval)"

                throw RuntimeException(timeoutMessage, lastException ?: lastAssertionError)
            }

            try {
                sleeper.sleep(Duration.ofMillis(pollingPeriod))
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                throw RuntimeException(e)
            }
        }
    }
}

class Sleeper {
    @Throws(InterruptedException::class)
    fun sleep(duration: Duration) {
        Thread.sleep(duration.toMillis())
    }
}