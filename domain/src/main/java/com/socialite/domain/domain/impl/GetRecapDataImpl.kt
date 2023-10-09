package com.socialite.domain.domain.impl

import com.socialite.common.utility.di.IoDispatcher
import com.socialite.data.repository.OrderDetailsRepository
import com.socialite.domain.domain.GetOrderListByReport
import com.socialite.domain.domain.GetOutcomes
import com.socialite.domain.domain.GetRecapData
import com.socialite.domain.helper.ProductOrderDetailConverter
import com.socialite.domain.helper.toDomain
import com.socialite.schema.ui.helper.Income
import com.socialite.schema.ui.helper.OrderWithProduct
import com.socialite.schema.ui.helper.ProductOrderDetail
import com.socialite.schema.ui.helper.RecapData
import com.socialite.schema.ui.helper.ReportParameter
import com.socialite.schema.ui.main.Order
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRecapDataImpl @Inject constructor(
    private val getOutcomes: GetOutcomes,
    private val orderDetailsRepository: OrderDetailsRepository,
    private val getOrderListByReport: GetOrderListByReport,
    private val converter: ProductOrderDetailConverter,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : GetRecapData {
    override fun invoke(parameters: ReportParameter): Flow<RecapData> {
        return flow {
            val incomes = getOrderListByReport(Order.DONE, parameters).first().map {
                val products = getProductOrder(it.order.id)
                val orderWithProduct = OrderWithProduct(it.toDomain(), products)

                Income(
                    date = it.order.orderTime,
                    total = orderWithProduct.grandTotalWithPromo,
                    payment = it.payment?.name ?: "",
                    isCash = it.payment?.isCash ?: false
                )
            }
            val outcomes = getOutcomes(parameters).first()

            emit(
                RecapData(
                    incomes = incomes,
                    outcomes = outcomes
                )
            )
        }.flowOn(dispatcher)
    }

    private suspend fun getProductOrder(orderId: String): List<ProductOrderDetail> {
        return orderDetailsRepository.getOrderDetailByIdOrder(orderId)
            .map {
                converter.convert(it)
            }.flowOn(dispatcher).first()
    }
}
