package com.socialite.solite_pos.view.store.recap

import com.socialite.solite_pos.data.source.local.entity.helper.MenuOrderAmount
import com.socialite.solite_pos.data.source.local.entity.helper.RecapData
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Store
import com.socialite.solite_pos.utils.config.DateUtils
import com.socialite.solite_pos.utils.tools.helper.ReportsParameter

data class RecapViewState(
    val stores: List<Store>,
    val recap: RecapData,
    val menus: List<MenuOrderAmount>,
    val selectedDate: Pair<String, String>,
    val selectedStore: Store?
) {

    fun getMenuWithParameters() = MenusWithParameter(
        menus = menus,
        parameter = getParameters()
    )

    fun getParameters() = ReportsParameter(
        start = selectedDate.first,
        end = selectedDate.second,
        storeId = selectedStore?.id ?: ""
    )

    companion object {
        fun idle(): RecapViewState {
            val currentDate = DateUtils.currentDate
            return RecapViewState(
                stores = emptyList(),
                recap = RecapData.empty(),
                menus = emptyList(),
                selectedDate = Pair(currentDate, currentDate),
                selectedStore = null
            )
        }
    }
}