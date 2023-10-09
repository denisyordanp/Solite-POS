package com.socialite.domain.domain

import com.socialite.schema.database.helper.OrderData
import com.socialite.schema.ui.helper.ReportParameter
import kotlinx.coroutines.flow.Flow

fun interface GetOrderListByReport {
    operator fun invoke(status: Int, parameters: ReportParameter): Flow<List<OrderData>>
}