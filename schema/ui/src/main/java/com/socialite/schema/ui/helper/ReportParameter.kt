package com.socialite.schema.ui.helper

import com.socialite.schema.ui.main.Store
import com.socialite.schema.ui.main.User

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
