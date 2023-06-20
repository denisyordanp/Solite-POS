package com.socialite.solite_pos.data.source.domain

import com.socialite.solite_pos.data.source.local.entity.helper.OrderMenuWithOrders
import com.socialite.solite_pos.utils.tools.helper.ReportParameter
import kotlinx.coroutines.flow.Flow

fun interface GetOrdersMenuWithOrders {
    operator fun invoke(parameter: ReportParameter): Flow<List<OrderMenuWithOrders>>
}
