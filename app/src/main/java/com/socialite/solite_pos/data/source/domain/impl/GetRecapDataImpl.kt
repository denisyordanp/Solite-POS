package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.GetRecapData
import com.socialite.solite_pos.data.source.domain.GetProductOrder
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.Income
import com.socialite.solite_pos.data.source.local.entity.helper.RecapData
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.repository.OrdersRepository
import com.socialite.solite_pos.data.source.repository.OutcomesRepository
import com.socialite.solite_pos.utils.tools.helper.ReportsParameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class GetRecapDataImpl(
    private val repository: OrdersRepository,
    private val outcomesRepository: OutcomesRepository,
    private val getProductOrder: GetProductOrder
) : GetRecapData {
    override fun invoke(parameters: ReportsParameter): Flow<RecapData> {

        return flow {
            val incomes = repository.getOrderList(Order.DONE, parameters).first().map {
                val products = getProductOrder.invoke(it.order.id).first()
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
}
