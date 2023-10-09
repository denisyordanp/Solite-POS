package com.socialite.solite_pos.utils.config

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun Long.timeMilliSecondToDateFormat(dateFormat: String): String {
    val formatter = SimpleDateFormat(dateFormat, Locale("in", "ID"))

    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return formatter.format(calendar.time)
}
