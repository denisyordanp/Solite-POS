package com.socialite.solite_pos.view.store.recap

import com.socialite.solite_pos.data.source.local.entity.helper.MenuOrderAmount
import com.socialite.solite_pos.utils.tools.helper.ReportsParameter

data class MenusWithParameter(
    val menus: List<MenuOrderAmount>,
    val parameter: ReportsParameter
)
