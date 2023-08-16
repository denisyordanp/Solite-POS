package com.socialite.data.schema.response

import com.socialite.data.schema.room.new_master.Customer

data class CustomerResponse(
    val id: String,
    val isUploaded: Boolean,
    val name: String
) {
     fun toEntity(): Customer {
        return Customer(
            id = id,
            name = name,
            isUploaded = isUploaded
        )
    }
}
