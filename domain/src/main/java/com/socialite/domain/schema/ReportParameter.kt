package com.socialite.domain.schema

data class ReportParameter(
    val start: String,
    val end: String,
    val storeId: String,
    val userId: Long
) {
    fun isTodayOnly() = storeId.isEmpty() && userId == 0L
}
