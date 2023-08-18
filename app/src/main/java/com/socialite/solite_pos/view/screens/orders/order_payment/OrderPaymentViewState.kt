package com.socialite.solite_pos.view.screens.orders.order_payment

import com.socialite.solite_pos.data.schema.helper.OrderWithProduct
import com.socialite.solite_pos.data.schema.Promo
import com.socialite.solite_pos.data.schema.Payment

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
