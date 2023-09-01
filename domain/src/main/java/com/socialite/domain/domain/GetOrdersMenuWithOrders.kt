package com.socialite.domain.domain

import com.socialite.domain.schema.ReportParameter
import com.socialite.domain.schema.OrderMenuWithOrders
import kotlinx.coroutines.flow.Flow

fun interface GetOrdersMenuWithOrders {
    operator fun invoke(parameter: ReportParameter): Flow<List<OrderMenuWithOrders>>
}
