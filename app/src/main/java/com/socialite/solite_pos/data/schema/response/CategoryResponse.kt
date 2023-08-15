package com.socialite.solite_pos.data.schema.response

import com.socialite.solite_pos.data.source.local.entity.helper.ResponseData
import com.socialite.solite_pos.data.schema.room.new_master.Category

data class CategoryResponse(
    val desc: String,
    override val id: String,
    val isActive: Boolean,
    val isUploaded: Boolean,
    val name: String
) : ResponseData {

    override fun toEntity(): Category {
        return Category(
            id = id,
            name = name,
            desc = desc,
            isActive = isActive,
            isUploaded = isUploaded
        )
    }
}
