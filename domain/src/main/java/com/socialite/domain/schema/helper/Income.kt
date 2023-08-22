package com.socialite.domain.schema.helper

import com.socialite.domain.helper.DateUtils

data class Income(
    var date: String,
    var payment: String,
    var total: Long,
    var isCash: Boolean
) {

    fun dateString() = DateUtils.convertDateFromDb(date, DateUtils.DATE_WITH_DAY_WITHOUT_YEAR_FORMAT)
}
