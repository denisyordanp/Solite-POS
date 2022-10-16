package com.socialite.solite_pos.view.main.opening.orders

object OrderDetailDestinations {

    const val ORDER_NO = "order_no"

    fun orderDetail(orderNo: String): String = "order_detail/$orderNo"
    const val ORDERS = "orders"
    const val ORDER_DETAIL = "order_detail/{$ORDER_NO}"
}
