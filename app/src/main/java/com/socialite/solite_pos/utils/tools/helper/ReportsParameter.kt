package com.socialite.solite_pos.utils.tools.helper

import com.socialite.solite_pos.utils.config.DateUtils
import java.io.Serializable

data class ReportsParameter(
    val start: String,
    val end: String,
    val storeId: Long
) : Serializable {

    fun isTodayOnly() = storeId == DEFAULT_STORE_ID

    fun toTitle() = if (start == end) {
        DateUtils.convertDateFromDate(
            start,
            DateUtils.DATE_WITH_DAY_WITHOUT_YEAR_FORMAT
        )
    } else {
        "${
            DateUtils.convertDateFromDate(
                start,
                DateUtils.DATE_WITH_MONTH_FORMAT
            )
        } - ${
            DateUtils.convertDateFromDate(
                end,
                DateUtils.DATE_WITH_MONTH_FORMAT
            )
        }"
    }

    companion object {

        private const val DEFAULT_STORE_ID = -1L

        fun createTodayOnly(isWithTime: Boolean = false): ReportsParameter {
            val date = if (isWithTime) DateUtils.currentDateTime else DateUtils.currentDate
            return ReportsParameter(
                start = date,
                end = date,
                storeId = DEFAULT_STORE_ID
            )
        }
    }
}
