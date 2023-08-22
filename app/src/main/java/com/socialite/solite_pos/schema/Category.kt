package com.socialite.solite_pos.schema

import com.socialite.solite_pos.view.ui.DropdownItem
import com.socialite.data.schema.room.new_master.Category as DataCategory

data class Category(
    val id: String,
    override val name: String,
    val desc: String,
    val isActive: Boolean,
    val isUploaded: Boolean
) : DropdownItem {
    companion object {
        fun fromData(data: DataCategory): Category {
            return Category(
                data.id,
                data.name,
                data.desc,
                data.isActive,
                data.isUploaded
            )
        }
    }
}
