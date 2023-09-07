package com.socialite.solite_pos.view.screens.store.variant_product

import com.socialite.domain.schema.VariantWithOptions
import com.socialite.domain.schema.main.Product
import com.socialite.domain.schema.main.VariantProduct

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
