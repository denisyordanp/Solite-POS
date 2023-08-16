package com.socialite.solite_pos.data.schema.response

import com.socialite.solite_pos.data.schema.room.new_master.Customer
import com.socialite.data.schema.response.CustomerResponse as DataResponse

data class CustomerResponse(
    override val id: String,
    val isUploaded: Boolean,
    val name: String
): ResponseData {

    override fun toEntity(): Customer {
        return Customer(
            id = id,
            name = name,
            isUploaded = isUploaded
        )
    }

    fun toDataResponse(): DataResponse {
        return DataResponse(
            id, isUploaded, name
        )
    }
}
