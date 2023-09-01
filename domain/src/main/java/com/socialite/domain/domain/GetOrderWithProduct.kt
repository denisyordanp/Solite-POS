package com.socialite.domain.domain

import com.socialite.domain.schema.OrderWithProduct
import kotlinx.coroutines.flow.Flow

fun interface GetOrderWithProduct {
    operator fun invoke(orderId: String): Flow<OrderWithProduct?>
}
