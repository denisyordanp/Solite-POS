package com.socialite.domain.schema.main

import java.util.UUID

data class VariantProduct(
    val id: String,
    val variant: String,
    val variantOption: String,
    val product: String,
    val isUploaded: Boolean,
    val isDeleted: Boolean
) {

    companion object {

        fun createNewVariantProduct(
            variant: String,
            variantOption: String,
            product: String
        ) = VariantProduct(
            id = UUID.randomUUID().toString(),
            variant = variant,
            variantOption = variantOption,
            product = product,
            isUploaded = false,
            isDeleted = false
        )
    }
}
