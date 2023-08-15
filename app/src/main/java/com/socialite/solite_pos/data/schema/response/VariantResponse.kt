package com.socialite.solite_pos.data.schema.response

import com.socialite.solite_pos.data.source.local.entity.room.new_master.Variant

data class VariantResponse(
    val id: String,
    val isMust: Boolean,
    val isUploaded: Boolean,
    val name: String,
    val type: Int
) {
    fun toEntity(): Variant {
        return Variant(
            id = id,
            name = name,
            type = type,
            isMust = isMust,
            isUploaded = isUploaded
        )
    }
}
