package com.socialite.common.utility.extension

import com.socialite.common.utility.helper.DateUtils

fun String.toDateWithDayWithoutYear() = DateUtils.convertDateFromDb(this, DateUtils.DATE_WITH_DAY_WITHOUT_YEAR_FORMAT)

fun String.toDateWithTime() = DateUtils.convertDateFromDb(this, DateUtils.DATE_WITH_TIME_FORMAT)