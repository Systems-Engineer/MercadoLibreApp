package com.brahian.mercadolibreapp.screen

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.brahian.mercadolibreapp.R
import com.brahian.mercadolibreapp.util.Wait
import org.hamcrest.Matchers.not

object MainScreen {

    val recyclerView = withId(R.id.recyclerview)
    val layoutError = withId(R.id.layout_error)
    val progressBar = withId(R.id.progressbar)

    fun waitForScreen(){
        Wait(progressBar).until(matches(isDisplayed()))
        Wait(recyclerView).until(matches(isDisplayed()))
        onView(layoutError).check(matches(not(isDisplayed())))
        onView(progressBar).check(matches(not(isDisplayed())))
    }

}