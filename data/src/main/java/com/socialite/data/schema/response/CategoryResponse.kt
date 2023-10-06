package com.socialite.data.schema.response

import com.socialite.schema.database.new_master.Category

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
