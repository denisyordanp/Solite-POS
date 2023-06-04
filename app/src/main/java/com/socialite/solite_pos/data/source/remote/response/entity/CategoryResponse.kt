package com.socialite.solite_pos.data.source.remote.response.entity

import com.socialite.solite_pos.data.source.local.entity.room.new_master.Category

data class CategoryResponse(
    val desc: String,
    val id: String,
    val isActive: Boolean,
    val isUploaded: Boolean,
    val name: String
) {

    fun toEntity(): Category {
        return Category(
            id = id,
            name = name,
            desc = desc,
            isActive = isActive,
            isUploaded = isUploaded
        )
    }
}
