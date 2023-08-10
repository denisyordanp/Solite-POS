package com.socialite.solite_pos.view.screens.order_customer

object OrderCustomerDestinations {

    const val PRODUCT_ID = "product_id"
    fun selectVariants(productId: String): String = "select_variants/$productId"
    const val SELECT_VARIANTS = "select_variants/{$PRODUCT_ID}"
    const val SELECT_ITEMS = "select_items"
    const val SELECT_CUSTOMERS = "select_customers"
}
