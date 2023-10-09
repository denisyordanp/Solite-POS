package com.socialite.solite_pos.view.screens.store.variant_product

import com.socialite.schema.ui.helper.VariantWithOptions
import com.socialite.schema.ui.main.Product
import com.socialite.schema.ui.main.VariantProduct

data class VariantProductViewState(
    val variants: List<VariantWithOptions>,
    val product: Product?,
    val selectedProductOptions: List<VariantProduct>,
) {
    companion object {
        fun idle() = VariantProductViewState(
            variants = emptyList(),
            product = null,
            selectedProductOptions = emptyList()
        )
    }
}
