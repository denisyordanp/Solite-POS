package com.socialite.solite_pos.view.store

import com.socialite.solite_pos.utils.tools.helper.ReportParameter

object StoreDestinations {

    const val PRODUCT_ID = "product_id"
    private const val NEW_PRODUCT = "new_product"

    const val MAIN_STORE = "main_store"
    const val MASTER_STORES = "master_stores"
    const val MASTER_RECAP = "master_recap"
    const val MASTER_PRODUCT = "master_product"
    const val MASTER_VARIANTS = "master_variants"
    const val MASTER_CATEGORY = "master_category"
    const val MASTER_PAYMENT = "master_payment"
    const val MASTER_PROMO = "master_promo"

    const val DETAIL_PRODUCT = "detail_product/{$PRODUCT_ID}"
    const val PRODUCT_VARIANTS = "product_variants/{$PRODUCT_ID}"
    val OUTCOMES = "outcomes/${ReportParameter.getRoute()}"
    fun productDetail(productId: String) = "detail_product/$productId"
    fun newProduct() = "detail_product/$NEW_PRODUCT"
    fun productVariants(productId: String) = "product_variants/$productId"
    fun isNewProduct(productId: String) = productId == NEW_PRODUCT
    fun outcomes(reportParameter: ReportParameter) = "outcomes/${reportParameter.createRoute()}"
}
