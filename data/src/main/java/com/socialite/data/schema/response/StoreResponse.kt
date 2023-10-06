package com.socialite.data.schema.response

import com.socialite.schema.database.new_master.Store

data class StoreResponse(
    val address: String,
    override val id: String,
    val isUploaded: Boolean,
    val name: String
) : ResponseData {

    override fun toEntity(): Store {
        return Store(
            id = id,
            name = name,
            address = address,
            isUploaded = isUploaded
        )
    }
}
