package com.socialite.solite_pos.data.source.remote.response.entity

import com.socialite.solite_pos.data.source.local.entity.room.master.Customer

data class CustomerResponse(
    val id: String,
    val isUploaded: Boolean,
    val name: String
) {

    fun toEntity(): Customer {
        return Customer(
            id = id.toLong(),
            name = name,
            isUploaded = isUploaded
        )
    }
}
