package com.socialite.solite_pos.data.domain.impl

import com.socialite.solite_pos.data.domain.GetRecapData
import com.socialite.solite_pos.data.schema.helper.Income
import com.socialite.solite_pos.data.schema.helper.OrderWithProduct
import com.socialite.solite_pos.data.schema.helper.ProductOrderDetail
import com.socialite.solite_pos.data.schema.helper.RecapData
import com.socialite.data.schema.room.master.Order
import com.socialite.data.repository.OrderDetailsRepository
import com.socialite.data.repository.OrdersRepository
import com.socialite.data.repository.OutcomesRepository
import com.socialite.solite_pos.data.schema.Outcome
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
            val incomes = repository.getOrderList(Order.DONE, parameters.toDataReport()).first().map {
                val products = getProductOrder(it.order.id)
                val orderWithProduct = OrderWithProduct(it, products)

                Income(
                    date = it.order.orderTime,
                    total = orderWithProduct.grandTotalWithPromo,
                    payment = it.payment?.name ?: "",
                    isCash = it.payment?.isCash ?: false
                )
            }
            val outcomes = outcomesRepository.getOutcomes(parameters.toDataReport()).first()

            emit(
                RecapData(
                    incomes = incomes,
                    outcomes = outcomes.map { Outcome.fromData(it) }
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
