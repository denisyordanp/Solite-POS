package com.socialite.schema.ui.helper

import com.socialite.schema.ui.main.Customer
import com.socialite.schema.ui.main.Order
import com.socialite.schema.ui.main.OrderPayment
import com.socialite.schema.ui.main.OrderPromo
import com.socialite.schema.ui.main.Payment
import com.socialite.schema.ui.main.Promo
import com.socialite.schema.ui.main.Store
import java.io.Serializable

data class OrderData(
    val order: Order,
    val store: Store,
    val customer: Customer,
    val orderPayment: OrderPayment?,
    val payment: Payment?,
    val orderPromo: OrderPromo?,
    val promo: Promo?,
) : Serializable {

    companion object {
        fun newOrder(order: Order, customer: Customer, store: Store) = OrderData(
            order = order,
            customer = customer,
            store = store,
            orderPayment = null,
            payment = null,
            orderPromo = null,
            promo = null
        )
    }
}
