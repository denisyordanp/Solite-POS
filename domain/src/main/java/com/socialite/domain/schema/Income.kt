package com.socialite.domain.schema

import com.socialite.domain.helper.DateUtils

data class Income(
    val date: String,
    val payment: String,
    val total: Long,
    val isCash: Boolean
) {

    fun dateString() = DateUtils.convertDateFromDb(date, DateUtils.DATE_WITH_DAY_WITHOUT_YEAR_FORMAT)
}
