package com.socialite.solite_pos.utils.config

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    companion object{
        private const val dbDateTimeFormat = "yyyy-MM-dd HH:mm:ss"
        private const val dbDateFormat = "yyyy-MM-dd"
        const val dateWithTimeFormat = "dd MMMM yyyy HH:mm"
        const val DATE_WITH_DAY_FORMAT = "EEE, dd MMMM yyyy"
        const val DATE_WITH_DAY_WITHOUT_YEAR_FORMAT = "EEE, d MMM"
        const val DATE_WITH_DAY_AND_TIME_FORMAT = "EEE, dd MMMM yyyy HH:mm"
        const val DATE_ORDER_NO_FORMAT = "ddMMyy"

        private val dbDateTimeSimpleFormat = SimpleDateFormat(dbDateTimeFormat, locale)
        private val dbDateSimpleFormat = SimpleDateFormat(dbDateFormat, locale)

        private val locale: Locale
            get() = Locale("in", "ID")

        fun convertDateFromDb(date: String?, format: String): String {
            return if (!date.isNullOrEmpty()) {
                try {
                    val ld = SimpleDateFormat(format, locale)
                    val d = dbDateTimeSimpleFormat.parse(date)
                    if (d != null) ld.format(d) else ""
                } catch (e: Exception) {
                    e.printStackTrace()
                    ""
                }
            } else {
                ""
            }
        }

        fun convertDateFromDate(date: String?, format: String): String {
            return if (!date.isNullOrEmpty()) {
                val ld = SimpleDateFormat(format, locale)
                val d = dbDateSimpleFormat.parse(date)
                if (d != null) ld.format(d) else ""
            } else {
                ""
            }
        }

        fun calendarToStr(calendar: Calendar): String {
            return dbDateTimeSimpleFormat.format(calendar.time)
        }

        fun strToCalendar(calendar: String): Calendar {
            val cal = Calendar.getInstance()
            val date = dbDateSimpleFormat.parse(calendar)
            if (date != null) cal.time = date
            return cal
        }

        fun strToDate(date: String?): Date {
            return if (date.isNullOrEmpty()) {
                Date()
            } else {
                return dbDateTimeSimpleFormat.parse(date)!!
            }
        }

        val currentDate: String
            get() {
                return dbDateSimpleFormat.format(currentTime)
            }

        val currentDateTime: String
            get() {
                return dbDateTimeSimpleFormat.format(currentTime)
            }

        val currentTime: Date
            get() = Calendar.getInstance().time


    }
}
