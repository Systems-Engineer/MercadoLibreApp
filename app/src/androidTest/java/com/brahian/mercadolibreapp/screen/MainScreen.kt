package com.brahian.mercadolibreapp.screen

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.brahian.mercadolibreapp.R
import com.brahian.mercadolibreapp.ui.view.ProductAdapter
import com.brahian.mercadolibreapp.util.Wait
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not

object MainScreen {

    private val recyclerView = withId(R.id.recyclerview)
    private val layoutError = withId(R.id.layout_error)
    private val progressBar = withId(R.id.progressbar)
    private val imageError = withId(R.id.imageview_error)
    private val textError = withId(R.id.textview_error)

    fun waitForScreen(){
        onView(progressBar).check(matches(isDisplayed()))
        Wait(progressBar).until(matches(not(isDisplayed())))
        onView(recyclerView).check(matches(isDisplayed()))
        onView(layoutError).check(matches(not(isDisplayed())))
    }

    fun waitForScreenToFail(error : String) {
        onView(progressBar).check(matches(isDisplayed()))
        Wait(progressBar).until(matches(not(isDisplayed())))
        onView(recyclerView).check(matches(not(isDisplayed())))
        onView(layoutError).check(matches(isDisplayed()))
        onView(imageError).check(matches(isDisplayed()))
        onView(textError).check(matches(allOf(isDisplayed(), withText(error))))
    }

    fun tapOnProduct() {
        onView(recyclerView).perform(RecyclerViewActions
            .actionOnItemAtPosition<ProductAdapter.ViewHolder>(0, click()))
    }

}