package com.brahian.mercadolibreapp.util

import android.widget.TextView
import java.text.NumberFormat
import java.util.*

fun TextView.formatToCurrency(value : Double?) {
    val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
    text = format.format(value)
}