package com.socialite.solite_pos.data.schema.response

import com.socialite.solite_pos.data.source.local.entity.room.new_master.VariantOption

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
