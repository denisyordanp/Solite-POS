package com.socialite.data.schema.response

import com.socialite.schema.database.new_master.VariantOption

data class VariantOptionResponse(
    val desc: String,
    val id: String,
    val isActive: Boolean,
    val isUploaded: Boolean,
    val name: String,
    val variant: String
) {
    fun toEntity(): VariantOption {
        return VariantOption(
            id = id,
            variant = variant,
            name = name,
            desc = desc,
            isActive = isActive,
            isUploaded = isUploaded
        )
    }
}