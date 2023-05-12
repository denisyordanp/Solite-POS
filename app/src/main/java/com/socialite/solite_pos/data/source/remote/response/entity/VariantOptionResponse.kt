package com.socialite.solite_pos.data.source.remote.response.entity

import com.socialite.solite_pos.data.source.local.entity.room.master.VariantOption

data class VariantOptionResponse(
    val desc: String,
    val id: String,
    val isActive: Boolean,
    val isCount: Boolean,
    val isUploaded: Boolean,
    val name: String,
    val variant: String
) {
    fun toEntity(): VariantOption {
        return VariantOption(
            id = id.toLong(),
            idVariant = variant.toLong(),
            name = name,
            desc = desc,
            isCount = isCount,
            isActive = isActive,
            isUploaded = isUploaded
        )
    }
}
