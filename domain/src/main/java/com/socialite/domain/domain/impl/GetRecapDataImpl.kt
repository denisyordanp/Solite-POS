package com.socialite.domain.domain.impl

import com.socialite.data.repository.OrderDetailsRepository
import com.socialite.data.repository.OrdersRepository
import com.socialite.data.repository.OutcomesRepository
import com.socialite.data.schema.room.new_master.Order
import com.socialite.domain.domain.GetRecapData
import com.socialite.domain.helper.ProductOrderDetailConverter
import com.socialite.domain.schema.Outcome
import com.socialite.domain.schema.ReportParameter
import com.socialite.domain.schema.helper.Income
import com.socialite.domain.schema.helper.OrderWithProduct
import com.socialite.domain.schema.helper.ProductOrderDetail
import com.socialite.domain.schema.helper.RecapData
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
