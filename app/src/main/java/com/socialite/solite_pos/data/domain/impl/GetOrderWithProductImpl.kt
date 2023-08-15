package com.socialite.solite_pos.data.domain.impl

import com.socialite.solite_pos.data.domain.GetOrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.repository.OrderDetailsRepository
import com.socialite.solite_pos.data.source.repository.OrdersRepository
import com.socialite.solite_pos.utils.tools.ProductOrderDetailConverter
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetOrderWithProductImpl @Inject constructor(
    private val orderRepository: OrdersRepository,
    private val orderDetailRepository: OrderDetailsRepository,
    private val converter: ProductOrderDetailConverter
) : GetOrderWithProduct {
    override fun invoke(orderId: String) = orderRepository.getOrderDataAsFlow(orderId)
        .combine(
            orderDetailRepository.getOrderDetailByIdOrder(orderId)
                .map { converter.convert(it) }
        ) { order, details ->
            order?.let {
                OrderWithProduct(
                    orderData = order,
                    products = details
                )
            }
        }
}
