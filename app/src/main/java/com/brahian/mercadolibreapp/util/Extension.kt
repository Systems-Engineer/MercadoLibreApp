package com.brahian.mercadolibreapp.util

import android.view.View.*
import android.widget.TextView
import java.text.NumberFormat
import java.util.*

fun TextView.formatToCurrency(value : Double?) {
    val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
    text = format.format(value)
}

fun TextView.setTextAndCapitalize(textValue : String?) {
    setVisibleText(textValue?.capitalize(Locale.getDefault()))
}

fun TextView.setVisibleText(textValue: String?) {
    if (!textValue.isNullOrBlank()) {
        text = textValue
        visibility = VISIBLE
    } else {
        visibility = GONE
    }
}