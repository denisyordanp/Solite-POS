package com.socialite.domain.domain

import com.socialite.schema.ui.helper.OrderWithProduct
import kotlinx.coroutines.flow.Flow

fun interface GetOrderWithProduct {
    operator fun invoke(orderId: String): Flow<OrderWithProduct?>
}
