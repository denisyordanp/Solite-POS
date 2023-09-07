package com.socialite.data.schema.helper

data class ReportParameter(
    val start: String,
    val end: String,
    val storeId: String
) {
    fun isTodayOnly() = storeId.isEmpty()
}
