package com.socialite.solite_pos.schema

import com.socialite.domain.helper.DateUtils
import com.socialite.solite_pos.view.ui.OrderMenus
import java.io.Serializable

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
}
