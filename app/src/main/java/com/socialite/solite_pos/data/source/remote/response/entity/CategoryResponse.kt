package com.socialite.solite_pos.data.source.remote.response.entity

import com.socialite.solite_pos.data.source.local.entity.room.master.Category

data class CategoryResponse(
    val desc: String,
    val id: String,
    val isActive: Boolean,
    val isStock: Boolean,
    val isUploaded: Boolean,
    val name: String
) {

    fun toEntity(): Category {
        return Category(
            id = id.toLong(),
            name = name,
            desc = desc,
            isStock = isStock,
            isActive = isActive,
            isUploaded = isUploaded
        )
    }
}
