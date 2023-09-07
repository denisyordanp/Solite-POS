package com.socialite.domain.schema

import com.socialite.domain.schema.main.Customer
import com.socialite.domain.schema.main.Order
import com.socialite.domain.schema.main.OrderPayment
import com.socialite.domain.schema.main.OrderPromo
import com.socialite.domain.schema.main.Payment
import com.socialite.domain.schema.main.Promo
import com.socialite.domain.schema.main.Store
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
