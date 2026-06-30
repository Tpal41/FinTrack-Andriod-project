package com.fintrack.app.core.util

import java.text.NumberFormat
import java.util.Locale
import kotlin.math.absoluteValue

fun formatMoney(amount: Double, currency: String = "₹"): String {
    val formatted = NumberFormat.getNumberInstance(Locale("en", "IN")).format(amount.absoluteValue)
    return if (amount < 0) "-$currency$formatted" else "$currency$formatted"
}

fun Double.asMoney(currency: String = "₹"): String = formatMoney(this, currency)
