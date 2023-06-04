package com.socialite.solite_pos.data.source.remote.response.entity

import com.socialite.solite_pos.data.source.local.entity.room.new_master.Store

data class StoreResponse(
    val address: String,
    val id: String,
    val isUploaded: Boolean,
    val name: String
) {

    fun toEntity(): Store {
        return Store(
            id = id,
            name = name,
            address = address,
            isUploaded = isUploaded
        )
    }
}
