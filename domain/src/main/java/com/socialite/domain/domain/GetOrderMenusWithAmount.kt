package com.socialite.domain.domain

import com.socialite.domain.schema.ReportParameter
import com.socialite.domain.schema.helper.MenuOrderAmount
import kotlinx.coroutines.flow.Flow

fun interface GetOrderMenusWithAmount {
    operator fun invoke(parameters: ReportParameter): Flow<List<MenuOrderAmount>>
}
