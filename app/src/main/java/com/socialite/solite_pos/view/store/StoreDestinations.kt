package com.socialite.solite_pos.view.store

object StoreDestinations {

    const val PRODUCT_ID = "product_id"

    const val MAIN_STORE = "main_store"
    const val MASTER_STORES = "master_stores"
    const val MASTER_RECAP = "master_recap"
    const val MASTER_PRODUCT = "master_product"
    const val MASTER_VARIANTS = "master_variants"

    const val DETAIL_PRODUCT = "detail_product/{$PRODUCT_ID}"
//    const val PRODUCT_VARIANTS = "product_variants/{$PRODUCT_ID}"
    fun productDetail(productId: Long) = "detail_product/$productId"
//    fun productVariants(productId: Long) = "product_variants/$productId"
}
