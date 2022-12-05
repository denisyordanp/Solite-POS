package com.socialite.solite_pos.data.source.local.entity.room.helper

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderPayment
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderPromo
import com.socialite.solite_pos.data.source.local.entity.room.master.Customer
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.local.entity.room.master.Payment
import com.socialite.solite_pos.data.source.local.entity.room.master.Promo
import java.io.Serializable

data class OrderData(
    @Embedded
    val order: Order,

    @Relation(parentColumn = Customer.ID, entityColumn = Customer.ID)
    val customer: Customer,

    @Relation(parentColumn = Order.NO, entityColumn = Order.NO)
    val orderPayment: OrderPayment?,

    @Relation(
        parentColumn = Order.NO,
        entity = Payment::class,
        entityColumn = Payment.ID,
        associateBy = Junction(
            value = OrderPayment::class,
            parentColumn = Order.NO,
            entityColumn = Payment.ID
        )
    ) val payment: Payment?,

    @Relation(parentColumn = Order.NO, entityColumn = Order.NO)
    val orderPromo: OrderPromo?,

    @Relation(
        parentColumn = Order.NO,
        entity = Promo::class,
        entityColumn = Promo.ID,
        associateBy = Junction(
            value = OrderPromo::class,
            parentColumn = Order.NO,
            entityColumn = Promo.ID
        )
    ) val promo: Promo?,
) : Serializable {

    companion object {
        fun newOrder(order: Order, customer: Customer) = OrderData(
            order = order,
            customer = customer,
            orderPayment = null,
            payment = null,
            orderPromo = null,
            promo = null
        )
    }
}
