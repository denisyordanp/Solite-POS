package com.socialite.solite_pos.utils.tools.helper

import android.os.Bundle
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.socialite.solite_pos.utils.config.DateUtils
import java.io.Serializable

data class ReportParameter(
    val start: String,
    val end: String,
    val storeId: String
) : Serializable {

    fun isTodayOnly() = storeId.isEmpty()

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

    fun createRoute() = "$start/$end/${storeId.ifEmpty { EMPTY_ROUTE_VALUE }}"

    companion object {

        private const val START = "report_start"
        private const val END = "report_end"
        private const val STORE = "report_store"
        private const val EMPTY_ROUTE_VALUE = "empty"

        fun createReportFromArguments(
            bundle: Bundle?
        ): ReportParameter {
            return bundle?.let {
                val start = it.getString(START) ?: ""
                val end = it.getString(END) ?: ""
                val store = it.getString(STORE) ?: ""
                ReportParameter(
                    start = start,
                    end = end,
                    storeId = if (store == EMPTY_ROUTE_VALUE) "" else store
                )
            } ?: createTodayOnly(true)
        }

        fun getRoute() = "{$START}/{$END}/{$STORE}"

        fun getArguments() = listOf(
            navArgument(name = START) { type = NavType.StringType },
            navArgument(name = END) { type = NavType.StringType },
            navArgument(name = STORE) {
                type = NavType.StringType
                defaultValue = ""
            },
        )

        fun createTodayOnly(isWithTime: Boolean = false): ReportParameter {
            val date = if (isWithTime) DateUtils.currentDateTime else DateUtils.currentDate
            return ReportParameter(
                start = date,
                end = date,
                storeId = ""
            )
        }
    }
}
