package com.socialite.solite_pos.data.domain

import com.socialite.solite_pos.data.schema.helper.MenuOrderAmount
import com.socialite.solite_pos.utils.tools.helper.ReportParameter
import kotlinx.coroutines.flow.Flow

fun interface GetOrderMenusWithAmount {
    operator fun invoke(parameters: ReportParameter): Flow<List<MenuOrderAmount>>
}
