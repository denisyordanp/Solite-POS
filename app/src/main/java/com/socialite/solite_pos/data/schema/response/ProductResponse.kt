package com.socialite.solite_pos.data.schema.response

import com.socialite.data.schema.response.ProductResponse as DataProductResponse
import com.socialite.solite_pos.data.schema.room.new_master.Product

data class ProductResponse(
    val category: String,
    val desc: String,
    val id: String,
    val image: String,
    val isActive: Boolean,
    val isUploaded: Boolean,
    val name: String,
    val price: Int,
) {
    fun toEntity(): Product {
        return Product(
            id = id,
            name = name,
            category = category,
            image = image,
            desc = desc,
            price = price.toLong(),
            isActive = isActive,
            isUploaded = isUploaded
        )
    }
}
