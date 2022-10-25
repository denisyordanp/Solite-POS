package com.socialite.solite_pos.view.orders

object OrderDetailDestinations {

    const val ORDER_NO = "order_no"

    fun orderDetail(orderNo: String): String = "order_detail/$orderNo"
    fun orderPayment(orderNo: String): String = "order_payment/$orderNo"

    const val ORDERS = "orders"
    const val ORDER_DETAIL = "order_detail/{$ORDER_NO}"
    const val ORDER_PAYMENT = "order_payment/{$ORDER_NO}"
}
