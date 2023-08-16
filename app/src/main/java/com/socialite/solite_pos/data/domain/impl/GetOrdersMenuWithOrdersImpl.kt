package com.socialite.solite_pos.data.domain.impl

import com.socialite.solite_pos.data.domain.GetOrdersMenuWithOrders
import com.socialite.solite_pos.data.schema.helper.OrderMenuWithOrders
import com.socialite.solite_pos.data.schema.helper.OrderWithProduct
import com.socialite.solite_pos.data.repository.OrderDetailsRepository
import com.socialite.solite_pos.data.repository.OrdersRepository
import com.socialite.solite_pos.utils.tools.ProductOrderDetailConverter
import com.socialite.solite_pos.utils.tools.helper.ReportParameter
import com.socialite.solite_pos.view.ui.OrderMenus
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
            orderRepository.getAllOrderList(parameter),
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
