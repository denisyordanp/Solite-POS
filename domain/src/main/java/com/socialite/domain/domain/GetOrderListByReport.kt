package com.socialite.domain.domain

import com.socialite.data.schema.room.helper.OrderData
import com.socialite.domain.schema.ReportParameter
import kotlinx.coroutines.flow.Flow

fun interface GetOrderListByReport {
    operator fun invoke(status: Int, parameters: ReportParameter): Flow<List<OrderData>>
}