package com.socialite.solite_pos.data.source.domain

import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import kotlinx.coroutines.flow.Flow

interface GetProductOrder {
    suspend operator fun invoke(orderNo: String): Flow<List<ProductOrderDetail>>
}
