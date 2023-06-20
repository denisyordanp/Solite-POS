package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.GetProductOrder
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.repository.OrderDetailsRepository
import com.socialite.solite_pos.utils.tools.ProductOrderDetailConverter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetProductOrderImpl(
    private val orderDetailRepository: OrderDetailsRepository,
    private val converter: ProductOrderDetailConverter
) : GetProductOrder {
    override fun invoke(orderId: String): Flow<List<ProductOrderDetail>> {
        return orderDetailRepository.getOrderDetailByIdOrder(orderId)
            .map {
                converter.convert(it)
            }
    }
}
