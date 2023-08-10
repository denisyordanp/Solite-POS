package com.socialite.solite_pos.data.source.local.entity.room.helper

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderPayment
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderPromo
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Customer
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Order
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Payment
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Promo
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Store
import java.io.Serializable

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
