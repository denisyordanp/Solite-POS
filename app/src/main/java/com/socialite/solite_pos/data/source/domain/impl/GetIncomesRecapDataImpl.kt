package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.GetIncomesRecapData
import com.socialite.solite_pos.data.source.domain.GetProductOrder
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.RecapData
import com.socialite.solite_pos.data.source.repository.OrdersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single

class GetIncomesRecapDataImpl(
    private val repository: OrdersRepository,
    private val getProductOrder: GetProductOrder
) : GetIncomesRecapData {
    override suspend fun invoke(status: Int, date: String): Flow<List<RecapData>> {
        return flow {
            val orders = repository.getOrderList(status, date).single()
            orders.map {
                val products = getProductOrder.invoke(it.order.orderNo).single()
                val orderWithProduct = OrderWithProduct(it, products)

                RecapData(
                    orderWithProduct.order.order.orderNo,
                    orderWithProduct.order.payment?.name,
                    orderWithProduct.grandTotal,
                    orderWithProduct.order.payment?.isCash
                )
            }
        }
    }
}
