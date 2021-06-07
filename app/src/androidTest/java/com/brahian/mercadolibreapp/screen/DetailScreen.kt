package com.brahian.mercadolibreapp.screen

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.brahian.mercadolibreapp.R
import com.brahian.mercadolibreapp.util.Wait
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matchers.not

object DetailScreen {

    private val attributesRecyclerView = withId(R.id.recyclerview_attributes)
    private val sellerProgressBar = withId(R.id.progressbar_seller)
    private val sellerName = withId(R.id.textview_seller_name)
    private val sellerReputation = withId(R.id.textview_seller_reputation)
    private val conditionAndSoldText = withId(R.id.textview_condition_sold)
    private val titleText = withId(R.id.textview_title)
    private val shippingAndPagoText = withId(R.id.textview_shipping_pago)

    fun waitForScreen(){
        Wait(attributesRecyclerView).until(matches(isDisplayed()))
        onView(sellerProgressBar).check(matches(isDisplayed()))
    }

    fun verifyDetails(conditionAndSold : String, title : String, shippingAndPago : String) {
        onView(conditionAndSoldText).check(matches(allOf(isDisplayed(), withText(conditionAndSold))))
        onView(titleText).check(matches(allOf(isDisplayed(), withText(title))))
        onView(shippingAndPagoText).check(matches(allOf(isDisplayed(), withText(shippingAndPago))))
    }

    fun verifySellerLoads(name : String, reputation : String){
        onView(sellerProgressBar).check(matches(isDisplayed()))
        Wait(sellerProgressBar).until(matches(not(isDisplayed())))
        onView(sellerName).check(matches(allOf(isDisplayed(), withText(name))))
        onView(sellerReputation).check(matches(allOf(isDisplayed(), withText(reputation))))
    }

    fun verifySellerDidNotLoad(){
        onView(sellerProgressBar).check(matches(isDisplayed()))
        Wait(sellerProgressBar).until(matches(not(isDisplayed())))
        onView(sellerName).check(matches(not(isDisplayed())))
        onView(sellerReputation).check(matches(not(isDisplayed())))
    }

}