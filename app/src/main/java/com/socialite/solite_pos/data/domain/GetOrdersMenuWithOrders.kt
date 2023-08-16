package com.socialite.solite_pos.data.domain

import com.socialite.solite_pos.data.schema.helper.OrderMenuWithOrders
import com.socialite.solite_pos.utils.tools.helper.ReportParameter
import kotlinx.coroutines.flow.Flow

fun interface GetOrdersMenuWithOrders {
    operator fun invoke(parameter: ReportParameter): Flow<List<OrderMenuWithOrders>>
}
