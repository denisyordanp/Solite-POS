package com.socialite.domain.schema.main

import java.util.UUID

data class VariantProduct(
    var id: String,
    var variant: String,
    var variantOption: String,
    var product: String,
    var isUploaded: Boolean,
    var isDeleted: Boolean
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
