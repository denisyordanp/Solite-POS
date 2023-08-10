package com.socialite.solite_pos.view.screens.orders

object OrderDetailDestinations {

    const val ORDER_ID = "order_id"
    const val PRODUCT_ID = "product_id"

    fun orderDetail(orderNo: String): String = "order_detail/$orderNo"
    fun orderPayment(orderNo: String): String = "order_payment/$orderNo"
    fun orderEditProducts(orderNo: String): String = "order_edit_products/$orderNo"
    fun orderSelectVariants(productId: String): String = "order_select_variants/$productId"

    const val ORDERS = "orders"
    const val ORDER_DETAIL = "order_detail/{$ORDER_ID}"
    const val ORDER_PAYMENT = "order_payment/{$ORDER_ID}"
    const val ORDER_EDIT_PRODUCTS = "order_edit_products/{$ORDER_ID}"
    const val ORDER_SELECT_VARIANTS = "order_select_variants/{${PRODUCT_ID}}"
}
