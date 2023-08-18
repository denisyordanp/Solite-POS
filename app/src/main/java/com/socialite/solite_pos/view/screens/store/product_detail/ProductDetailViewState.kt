package com.socialite.solite_pos.view.screens.store.product_detail

import com.socialite.solite_pos.data.schema.Category

data class ProductDetailViewState(
    val categories: List<Category>,
    val product: ProductVariantOptions?
) {
    companion object {
        fun idle() = ProductDetailViewState(
            categories = emptyList(),
            product = null
        )
    }
}
