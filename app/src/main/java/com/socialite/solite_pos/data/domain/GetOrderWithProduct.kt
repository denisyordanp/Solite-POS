package com.socialite.solite_pos.data.domain

import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import kotlinx.coroutines.flow.Flow

fun interface GetOrderWithProduct {
    operator fun invoke(orderId: String): Flow<OrderWithProduct?>
}
