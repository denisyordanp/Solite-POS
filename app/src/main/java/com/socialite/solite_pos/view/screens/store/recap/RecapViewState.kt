package com.socialite.solite_pos.view.screens.store.recap

import com.socialite.domain.schema.RecapData
import com.socialite.solite_pos.schema.Store
import com.socialite.domain.helper.DateUtils
import com.socialite.solite_pos.schema.MenuOrderAmount
import com.socialite.solite_pos.schema.User
import com.socialite.solite_pos.utils.tools.helper.ReportParameter

data class RecapViewState(
    val stores: List<Store>,
    val users: List<User>,
    val recap: RecapData,
    val menus: List<MenuOrderAmount>,
    val selectedDate: Pair<String, String>,
    val selectedStore: Store?,
    val selectedUser: User?
) {

    fun getMenuWithParameters() = MenusWithParameter(
        menus = menus,
        parameter = getParameters()
    )

    fun getParameters() = ReportParameter(
        start = selectedDate.first,
        end = selectedDate.second,
        storeId = selectedStore?.id ?: "",
        userId = selectedUser?.id ?: ""
    )

    companion object {
        fun idle(): RecapViewState {
            val currentDate = DateUtils.currentDate
            return RecapViewState(
                stores = emptyList(),
                recap = RecapData.empty(),
                menus = emptyList(),
                selectedDate = Pair(currentDate, currentDate),
                selectedStore = null,
                selectedUser = null,
                users = emptyList()
            )
        }
    }
}
