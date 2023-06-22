package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.GetRecapData
import com.socialite.solite_pos.data.source.local.entity.helper.Income
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.helper.RecapData
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.repository.OrderDetailsRepository
import com.socialite.solite_pos.data.source.repository.OrdersRepository
import com.socialite.solite_pos.data.source.repository.OutcomesRepository
import com.socialite.solite_pos.utils.tools.ProductOrderDetailConverter
import com.socialite.solite_pos.utils.tools.helper.ReportParameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRecapDataImpl @Inject constructor(
    private val repository: OrdersRepository,
    private val outcomesRepository: OutcomesRepository,
    private val orderDetailsRepository: OrderDetailsRepository,
    private val converter: ProductOrderDetailConverter
) : GetRecapData {
    override fun invoke(parameters: ReportParameter): Flow<RecapData> {
        return flow {
            val incomes = repository.getOrderList(Order.DONE, parameters).first().map {
                val products = getProductOrder(it.order.id)
                val orderWithProduct = OrderWithProduct(it, products)

                Income(
                    date = it.order.orderTime,
                    total = orderWithProduct.grandTotalWithPromo,
                    payment = it.payment?.name ?: "",
                    isCash = it.payment?.isCash ?: false
                )
            }
            val outcomes = outcomesRepository.getOutcomes(parameters).first()

            emit(
                RecapData(
                    incomes = incomes,
                    outcomes = outcomes
                )
            )
        }
    }

    private suspend fun getProductOrder(orderId: String): List<ProductOrderDetail> {
        return orderDetailsRepository.getOrderDetailByIdOrder(orderId)
            .map {
                converter.convert(it)
            }.first()
    }
}
