package com.socialite.data.schema.room.helper

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.socialite.data.schema.room.new_bridge.OrderPayment
import com.socialite.data.schema.room.new_bridge.OrderPromo
import com.socialite.data.schema.room.new_master.Customer
import com.socialite.data.schema.room.new_master.Order
import com.socialite.data.schema.room.new_master.Payment
import com.socialite.data.schema.room.new_master.Promo
import com.socialite.data.schema.room.new_master.Store

data class OrderData(
    @Embedded
    val order: Order,

    @Relation(parentColumn = Store.ID, entityColumn = Store.ID)
    val store: Store,

    @Relation(parentColumn = Customer.ID, entityColumn = Customer.ID)
    val customer: Customer,

    @Relation(parentColumn = Order.ID, entityColumn = Order.ID)
    val orderPayment: OrderPayment?,

    @Relation(
        parentColumn = Order.ID,
        entity = Payment::class,
        entityColumn = Payment.ID,
        associateBy = Junction(
            value = OrderPayment::class,
            parentColumn = Order.ID,
            entityColumn = Payment.ID
        )
    ) val payment: Payment?,

    @Relation(parentColumn = Order.ID, entityColumn = Order.ID)
    val orderPromo: OrderPromo?,

    @Relation(
        parentColumn = Order.ID,
        entity = Promo::class,
        entityColumn = Promo.ID,
        associateBy = Junction(
            value = OrderPromo::class,
            parentColumn = Order.ID,
            entityColumn = Promo.ID
        )
    ) val promo: Promo?,
)
