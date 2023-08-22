package com.socialite.domain.schema

import com.socialite.data.schema.helper.ReportParameter as DataReport

data class ReportParameter(
    val start: String,
    val end: String,
    val storeId: String
) {

    fun toDataReport(): DataReport {
        return DataReport(
            start, end, storeId
        )
    }
}
