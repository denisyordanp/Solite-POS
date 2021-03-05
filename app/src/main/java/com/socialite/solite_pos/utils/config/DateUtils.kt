package com.socialite.solite_pos.utils.config

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    companion object{
        private const val databaseDateFormat = "yyyy-MM-dd HH:mm:ss"
        const val dateWithTimeFormat = "dd MMMM yyyy HH:mm"
        const val dateWithDayFormat = "EEE, dd MMMM yyyy"

        private val dbSimpleDateFormat = SimpleDateFormat(databaseDateFormat, locale)

        private val locale: Locale
        get() = Locale.getDefault()

        fun dateFormat(date: String?, format: String): String {
            return if (!date.isNullOrEmpty()) {
                val ld = SimpleDateFormat(format, locale)
                val d = dbSimpleDateFormat.parse(date)
                if (d != null) ld.format(d) else ""
            }else{
                ""
            }
        }

        fun strToDate(date: String?): Date{
            return if (date.isNullOrEmpty()){
                Date()
            }else{
                return dbSimpleDateFormat.parse(date)!!
            }
        }

        val currentTime: Date
            get() = Calendar.getInstance().time

        val currentDate: String
            get() {
                return dbSimpleDateFormat.format(currentTime)
            }
    }
}