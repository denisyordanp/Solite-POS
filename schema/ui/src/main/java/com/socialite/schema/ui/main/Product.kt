package com.socialite.schema.ui.main

import java.util.UUID

data class Product(
    val id: String,
    val name: String,
    val category: String,
    val image: String,
    val desc: String,
    val price: Long,
    val isActive: Boolean,
    val isUploaded: Boolean
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
