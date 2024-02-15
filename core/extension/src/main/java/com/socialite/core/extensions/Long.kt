package com.socialite.core.extensions

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
fun Long.toIDR(): String {
    return when {
        this >= 1000 -> {
            val kValue = this.toDouble() / 1000.0
            if (kValue % 1.0 == 0.0) {
                "IDR ${kValue.toInt()}K"
            } else {
                String.format("IDR %.1fK", kValue)
            }
        }
        else -> this.toString()
    }
}
fun Long.toKFormat() = this.thousand().rupiahToK()
fun Long.thousand(): String = String.format("%,d", this)
fun Long.timeMilliSecondToDateFormat(dateFormat: String): String {
    val formatter = SimpleDateFormat(dateFormat, Locale("in", "ID"))

    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return formatter.format(calendar.time)
}