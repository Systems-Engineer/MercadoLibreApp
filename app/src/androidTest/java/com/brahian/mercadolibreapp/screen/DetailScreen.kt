package com.brahian.mercadolibreapp.screen

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.brahian.mercadolibreapp.R
import com.brahian.mercadolibreapp.util.Wait
import org.hamcrest.Matchers.not

object DetailScreen {

    private val attributesRecyclerView = withId(R.id.recyclerview_attributes)
    private val sellerProgressBar = withId(R.id.progressbar_seller)
    private val sellerName = withId(R.id.textview_seller_name)
    private val sellerReputation = withId(R.id.textview_seller_reputation)

    fun waitForScreen(){
        Wait(attributesRecyclerView).until(matches(isDisplayed()))
        onView(sellerProgressBar).check(matches(isDisplayed()))
    }

    fun verifySellerLoads(){
        onView(sellerProgressBar).check(matches(isDisplayed()))
        Wait(sellerProgressBar).until(matches(not(isDisplayed())))
        onView(sellerName).check(matches(isDisplayed()))
        onView(sellerReputation).check(matches(isDisplayed()))
    }

}