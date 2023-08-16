package com.socialite.solite_pos.data.schema

import com.socialite.solite_pos.utils.config.DateUtils
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

    fun isEditable() = status == ON_PROCESS || status == NEED_PAY

    fun getQueueNumber(): String {
        return orderNo.substring(orderNo.length - 3, orderNo.length)
    }

    val timeString: String
        get() {
            return DateUtils.convertDateFromDb(orderTime, DateUtils.DATE_WITH_TIME_FORMAT)
        }

    fun statusToOrderMenu() = OrderMenus.values().find { it.status == status }

    companion object {

        const val ORDER_DATE = "order_date"
        const val TAKE_AWAY = "take_away"
        const val STATUS = "status"
        const val NO = "order_no"
        const val ID = "order_id"

        const val DB_NAME = "new_order"

        const val ON_PROCESS = 0
        const val NEED_PAY = 1
        const val CANCEL = 2
        const val DONE = 3

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
