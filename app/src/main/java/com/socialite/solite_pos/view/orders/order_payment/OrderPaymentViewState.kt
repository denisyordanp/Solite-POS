package com.socialite.solite_pos.view.orders.order_payment

import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Payment
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Promo

data class OrderPaymentViewState(
    val promos: List<Promo>,
    val payments: List<Payment>,
    val cashSuggestion: List<Long>?,
    val orderWithProduct: OrderWithProduct?
) {
    companion object {
        fun idle() = OrderPaymentViewState(
            promos = emptyList(),
            payments = emptyList(),
            cashSuggestion = null,
            orderWithProduct = null
        )
    }
}
