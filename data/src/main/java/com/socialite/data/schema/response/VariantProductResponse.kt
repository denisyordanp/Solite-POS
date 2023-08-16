package com.socialite.data.schema.response

import com.socialite.data.schema.room.new_bridge.VariantProduct

data class VariantProductResponse(
    val id: String,
    val isUploaded: Boolean,
    val product: String,
    val variant: String,
    val variantOption: String
) {
    fun toEntity(): VariantProduct {
        return VariantProduct(
            id = id,
            variant = variant,
            variantOption = variantOption,
            product = product,
            isUploaded = isUploaded,
            isDeleted = false
        )
    }
}
