package com.socialite.domain.schema

import com.socialite.domain.schema.main.Store
import com.socialite.domain.schema.main.User

data class ReportParameter(
    val start: String,
    val end: String,
    val storeId: String,
    val userId: Long
) {
    fun isTodayOnly() = storeId.isEmpty()
    fun isLoggedInUserOnly() = userId == 0L && storeId.isNotEmpty()
    fun isAllUser() = userId == User.ADD_OPTION_ID
    fun isAllStore() = storeId == Store.ADD_OPTION_ID
    fun isAllStoreAndUser() = isAllStore() && isAllUser()
}
