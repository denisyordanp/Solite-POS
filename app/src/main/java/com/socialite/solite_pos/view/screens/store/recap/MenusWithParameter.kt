package com.socialite.solite_pos.view.screens.store.recap

import com.socialite.solite_pos.schema.MenuOrderAmount
import com.socialite.solite_pos.utils.tools.helper.ReportParameter

data class MenusWithParameter(
    val menus: List<MenuOrderAmount>,
    val parameter: ReportParameter
)
