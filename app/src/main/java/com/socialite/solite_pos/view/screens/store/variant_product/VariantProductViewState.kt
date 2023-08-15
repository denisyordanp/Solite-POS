package com.socialite.solite_pos.view.screens.store.variant_product

import com.socialite.solite_pos.data.source.local.entity.helper.VariantWithOptions
import com.socialite.solite_pos.data.schema.room.new_bridge.VariantProduct
import com.socialite.solite_pos.data.schema.room.new_master.Product

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
