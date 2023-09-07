package com.socialite.domain.schema.main

import java.util.UUID

data class Product(
    val id: String,
    var name: String,
    var category: String,
    var image: String,
    var desc: String,
    var price: Long,
    var isActive: Boolean,
    var isUploaded: Boolean
) {
    companion object {
        fun createNewProduct(
            name: String,
            desc: String,
            price: Long,
            category: String
        ) = Product(
            id = UUID.randomUUID().toString(),
            name = name,
            desc = desc,
            price = price,
            category = category,
            image = "",
            isActive = true,
            isUploaded = false
        )
    }
}
