package com.socialite.solite_pos.data.source.domain

import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail

interface UpdateOrderProducts {
    suspend operator fun invoke(orderId: String, products: List<ProductOrderDetail>)
}
