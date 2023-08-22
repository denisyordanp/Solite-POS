package com.socialite.domain.domain.impl

import com.socialite.domain.domain.GetOrdersMenuWithOrders
import com.socialite.data.repository.OrderDetailsRepository
import com.socialite.data.repository.OrdersRepository
import com.socialite.domain.helper.ProductOrderDetailConverter
import com.socialite.domain.menu.OrderMenus
import com.socialite.domain.schema.ReportParameter
import com.socialite.domain.schema.helper.OrderMenuWithOrders
import com.socialite.domain.schema.helper.OrderWithProduct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetOrdersMenuWithOrdersImpl @Inject constructor(
    private val orderRepository: OrdersRepository,
    private val orderDetailRepository: OrderDetailsRepository,
    private val converter: ProductOrderDetailConverter
) : GetOrdersMenuWithOrders {
    override fun invoke(parameter: ReportParameter): Flow<List<OrderMenuWithOrders>> {
        return combine(
            flowOf(OrderMenus.values()),
            orderRepository.getAllOrderList(parameter.toDataReport()),
            orderDetailRepository.getOrderDetail()
        ) { menus, orders, details ->
            menus.map { menu ->
                val filteredOrders = orders.filter { it.order.status == menu.status }
                    .map { order ->
                        val filteredDetails = details.filter { it.order == order.order.id }
                        val productDetails = converter.convert(filteredDetails)
                        OrderWithProduct(
                            orderData = order,
                            products = productDetails
                        )
                    }
                OrderMenuWithOrders(
                    menu = menu,
                    orders = filteredOrders
                )
            }
        }
    }
}
