package com.socialite.solite_pos.schema

import com.socialite.domain.helper.DateUtils
import com.socialite.solite_pos.view.ui.OrderMenus
import java.io.Serializable
import com.socialite.data.schema.room.new_master.Order as OrderData

data class Order(
    val id: String,
    var orderNo: String,
    var customer: String,
    var orderTime: String,
    var isTakeAway: Boolean,
    var status: Int,
    var store: String,
    var isUploaded: Boolean
) : Serializable {

    fun getQueueNumber(): String {
        return orderNo.substring(orderNo.length - 3, orderNo.length)
    }

    val timeString: String
        get() {
            return DateUtils.convertDateFromDb(orderTime, DateUtils.DATE_WITH_TIME_FORMAT)
        }

    fun statusToOrderMenu() = OrderMenus.values().find { it.status == status }

    companion object {
        fun fromData(order: OrderData): Order {
            return Order(
                order.id,
                order.orderNo,
                order.customer,
                order.orderTime,
                order.isTakeAway,
                order.status,
                order.store,
                order.isUploaded
            )
        }
    }
}
