package com.socialite.domain.domain.impl

import com.socialite.common.utility.di.IoDispatcher
import com.socialite.data.repository.OrderDetailsRepository
import com.socialite.domain.domain.GetAllOrderListByReport
import com.socialite.domain.domain.GetOrdersMenuWithOrders
import com.socialite.domain.helper.ProductOrderDetailConverter
import com.socialite.domain.helper.toDomain
import com.socialite.schema.menu.OrderMenus
import com.socialite.domain.schema.OrderMenuWithOrders
import com.socialite.schema.ui.helper.OrderWithProduct
import com.socialite.schema.ui.helper.ReportParameter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetOrdersMenuWithOrdersImpl @Inject constructor(
    private val getAllOrderListByReport: GetAllOrderListByReport,
    private val orderDetailRepository: OrderDetailsRepository,
    private val converter: ProductOrderDetailConverter,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : GetOrdersMenuWithOrders {
    override fun invoke(parameter: ReportParameter): Flow<List<OrderMenuWithOrders>> {
        return combine(
            flowOf(OrderMenus.values()),
            getAllOrderListByReport(parameter),
            orderDetailRepository.getOrderDetail()
        ) { menus, orders, details ->
            menus.map { menu ->
                val filteredOrders = orders.filter { it.order.status == menu.status }
                    .map { order ->
                        val filteredDetails = details.filter { it.order == order.order.id }
                        val productDetails = converter.convert(filteredDetails)
                        OrderWithProduct(
                            orderData = order.toDomain(),
                            products = productDetails
                        )
                    }
                OrderMenuWithOrders(
                    menu = menu,
                    orders = filteredOrders
                )
            }
        }.flowOn(dispatcher)
    }
}
