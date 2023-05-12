package com.socialite.solite_pos.data.source.remote.response.entity

import com.socialite.solite_pos.data.source.local.entity.room.master.Variant

data class VariantResponse(
    val id: String,
    val isMix: Boolean,
    val isMust: Boolean,
    val isUploaded: Boolean,
    val name: String,
    val type: Int
) {
    fun toEntity(): Variant {
        return Variant(
            idVariant = id.toLong(),
            name = name,
            type = type,
            isMust = isMust,
            isMix = isMix,
            isUploaded = isUploaded
        )
    }
}
