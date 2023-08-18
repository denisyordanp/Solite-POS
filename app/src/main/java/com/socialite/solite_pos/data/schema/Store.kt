package com.socialite.solite_pos.data.schema

import com.socialite.solite_pos.view.ui.DropdownItem
import com.socialite.data.schema.room.new_master.Store as DataStore

data class Store(
    val id: String,
    override val name: String,
    val address: String,
    val isUploaded: Boolean
) : DropdownItem {
    companion object {
        fun fromData(data: DataStore): Store {
            return Store(
                data.id,
                data.name,
                data.address,
                data.isUploaded
            )
        }
    }
}
