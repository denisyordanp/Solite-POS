package com.socialite.data.schema.response

import com.socialite.data.schema.room.new_master.Variant

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
