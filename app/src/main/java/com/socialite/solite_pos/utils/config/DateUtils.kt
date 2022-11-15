package com.socialite.solite_pos.utils.config

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DateUtils {
    companion object {
        private const val dbDateTimeFormat = "yyyy-MM-dd HH:mm:ss"
        private const val dbDateFormat = "yyyy-MM-dd"
        const val DATE_WITH_TIME_FORMAT = "dd MMMM yyyy HH:mm"
        const val DATE_WITH_DAY_FORMAT = "EEE, dd MMMM yyyy"
        const val DATE_WITH_DAY_WITHOUT_YEAR_FORMAT = "EEE, d MMM"
        const val DATE_WITH_DAY_AND_TIME_FORMAT = "EEE, dd MMMM yyyy HH:mm"
        const val DATE_WITH_MONTH_FORMAT = "d MMM"
        const val DATE_ORDER_NO_FORMAT = "ddMMyy"
        const val HOUR_AND_TIME_FORMAT = "HH:mm"

        private val dbDateTimeSimpleFormat = SimpleDateFormat(dbDateTimeFormat, locale)
        private val dbDateSimpleFormat = SimpleDateFormat(dbDateFormat, locale)

        val locale: Locale
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

        fun millisToDate(millis: Long, isWithTime: Boolean = false): String {
            val cal = Calendar.getInstance()
            cal.timeInMillis = millis
            return if (isWithTime)
                dbDateTimeSimpleFormat.format(cal.time)
            else
                dbDateSimpleFormat.format(cal.time)
        }

        fun strToDate(date: String?): Date {
            return if (date.isNullOrEmpty()) {
                Date()
            } else {
                return dbDateTimeSimpleFormat.parse(date)!!
            }
        }

        fun isDateTimeIsToday(dateTime: String): Boolean {
            val currentDate = convertDateFromDb(dateTime, DATE_ORDER_NO_FORMAT)
            val todayDate = convertDateFromDb(currentDateTime, DATE_ORDER_NO_FORMAT)
            return currentDate == todayDate
        }

        fun strToHourAndMinute(dateTime: String): Pair<Int, Int> {
            val date = strToDate(dateTime)
            val cal = Calendar.getInstance()
            cal.time = date

            return Pair(
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE)
            )
        }

        fun strDateTimeReplaceTime(dateTime: String, hour: Int, minute: Int): String {
            val date = strToDate(dateTime)
            val cal = Calendar.getInstance()
            cal.time = date
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            return dbDateTimeSimpleFormat.format(cal.time)
        }

        fun strDateTimeReplaceDate(oldDate: String, newDate: String): String {
            val oldDateAsDate = strToDate(oldDate)
            val newDateAsDate = strToDate(newDate)

            val calOldDate = Calendar.getInstance()
            val calNewDate = Calendar.getInstance()

            calOldDate.time = oldDateAsDate
            calNewDate.time = newDateAsDate

            calNewDate.set(Calendar.HOUR_OF_DAY, calOldDate.get(Calendar.HOUR_OF_DAY))
            calNewDate.set(Calendar.MINUTE, calOldDate.get(Calendar.MINUTE))
            return dbDateTimeSimpleFormat.format(calNewDate.time)
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
