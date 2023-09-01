package com.socialite.domain.domain.impl

import com.socialite.data.repository.OrderDetailsRepository
import com.socialite.data.repository.OrdersRepository
import com.socialite.data.repository.OutcomesRepository
import com.socialite.data.schema.room.new_master.Order
import com.socialite.domain.domain.GetRecapData
import com.socialite.domain.helper.ProductOrderDetailConverter
import com.socialite.domain.helper.toData
import com.socialite.domain.helper.toDomain
import com.socialite.domain.schema.Income
import com.socialite.domain.schema.OrderWithProduct
import com.socialite.domain.schema.ProductOrderDetail
import com.socialite.domain.schema.RecapData
import com.socialite.domain.schema.ReportParameter
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
            val incomes = repository.getOrderList(Order.DONE, parameters.toData()).first().map {
                val products = getProductOrder(it.order.id)
                val orderWithProduct = OrderWithProduct(it.toDomain(), products)

                Income(
                    date = it.order.orderTime,
                    total = orderWithProduct.grandTotalWithPromo,
                    payment = it.payment?.name ?: "",
                    isCash = it.payment?.isCash ?: false
                )
            }
            val outcomes = outcomesRepository.getOutcomes(parameters.toData()).first()

            emit(
                RecapData(
                    incomes = incomes,
                    outcomes = outcomes.map { it.toDomain() }
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
