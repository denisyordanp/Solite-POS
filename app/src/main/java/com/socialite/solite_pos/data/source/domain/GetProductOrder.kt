package com.socialite.solite_pos.data.source.domain

import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import kotlinx.coroutines.flow.Flow

interface GetProductOrder {
    operator fun invoke(orderId: String): Flow<List<ProductOrderDetail>>
}
