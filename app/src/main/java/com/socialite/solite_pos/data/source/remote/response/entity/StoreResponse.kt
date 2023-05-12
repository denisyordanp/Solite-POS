package com.socialite.solite_pos.data.source.remote.response.entity

import com.socialite.solite_pos.data.source.local.entity.room.master.Store

data class StoreResponse(
    val address: String,
    val id: String,
    val isUploaded: Boolean,
    val name: String
) {

    fun toEntity(): Store {
        return Store(
            id = id.toLong(),
            name = name,
            address = address,
            isUploaded = isUploaded
        )
    }
}
