package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.GetRecapData
import com.socialite.solite_pos.data.source.domain.GetProductOrder
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.Income
import com.socialite.solite_pos.data.source.local.entity.helper.RecapData
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.repository.OrdersRepository
import com.socialite.solite_pos.data.source.repository.OutcomesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class GetRecapDataImpl(
    private val repository: OrdersRepository,
    private val outcomesRepository: OutcomesRepository,
    private val getProductOrder: GetProductOrder
) : GetRecapData {
    override fun invoke(from: String, until: String, store: Long): Flow<RecapData> {

        return flow {
            val incomes = repository.getOrderList(Order.DONE, from, until, store).first().map {
                val products = getProductOrder.invoke(it.order.orderNo).first()
                val orderWithProduct = OrderWithProduct(it, products)

                Income(
                    date = it.order.orderTime,
                    total = orderWithProduct.grandTotal,
                    payment = it.payment?.name ?: "",
                    isCash = it.payment?.isCash ?: false
                )
            }
            val outcomes = outcomesRepository.getOutcomes(from, until).first()

            emit(
                RecapData(
                    incomes = incomes,
                    outcomes = outcomes
                )
            )
        }

    }
}
