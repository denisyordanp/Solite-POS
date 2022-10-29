package com.socialite.solite_pos.utils.tools.helper

import com.socialite.solite_pos.utils.config.DateUtils
import java.io.Serializable

data class OrdersParameter(
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

        fun createTodayOnly() = OrdersParameter(
            DateUtils.currentDate,
            DateUtils.currentDate,
            DEFAULT_STORE_ID
        )
    }
}
