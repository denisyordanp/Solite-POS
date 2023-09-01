package com.socialite.domain.domain.impl

import com.socialite.domain.domain.GetOrderWithProduct
import com.socialite.data.repository.OrderDetailsRepository
import com.socialite.data.repository.OrdersRepository
import com.socialite.domain.helper.ProductOrderDetailConverter
import com.socialite.domain.helper.toDomain
import com.socialite.domain.schema.OrderWithProduct
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
                    orderData = order.toDomain(),
                    products = details
                )
            }
        }
}
