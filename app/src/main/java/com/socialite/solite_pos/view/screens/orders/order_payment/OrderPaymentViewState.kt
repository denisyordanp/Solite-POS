package com.socialite.solite_pos.view.screens.orders.order_payment

import com.socialite.solite_pos.data.schema.helper.OrderWithProduct
import com.socialite.data.schema.room.new_master.Payment
import com.socialite.data.schema.room.new_master.Promo

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
