package com.socialite.schema.ui.dummy

import com.socialite.schema.ui.main.Category
import com.socialite.schema.ui.main.Product

object DummySchema {
    val products = listOf(
        Product(
            name = "Siomay",
            desc = "Siomay ayam",
            price = 15000,
            category = "",
            image = "https://denisyordanp.com/public_assets/images/solite_pos_logo.png",
            id = "1",
            isActive = true,
            isUploaded = false
        ),
        Product(
            name = "Lorem ipsum dolor sit amet",
            desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
            price = 15000,
            category = "",
            image = "",
            id = "2",
            isActive = true,
            isUploaded = false
        ),
        Product(
            name = "Siomay",
            desc = "Siomay ayam",
            price = 15000,
            category = "",
            image = "https://denisyordanp.com/public_assets/images/solite_pos_logo.png",
            id = "3",
            isActive = true,
            isUploaded = false
        ),
        Product(
            name = "Lorem ipsum dolor sit amet",
            desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
            price = 15000,
            category = "",
            image = "",
            id = "4",
            isActive = true,
            isUploaded = false
        ),
    )

    val categoriesProducts = mapOf(
        Category(
            id = "1",
            name = "Makanan",
            desc = "",
            isActive = true,
            isUploaded = true
        ) to products,
        Category(
            id = "2",
            name = "Minuman",
            desc = "",
            isActive = true,
            isUploaded = true
        ) to products,
        Category(
            id = "3",
            name = "Coffee",
            desc = "",
            isActive = true,
            isUploaded = true
        ) to products,
        Category(
            id = "4",
            name = "Non-Coffee",
            desc = "",
            isActive = true,
            isUploaded = true
        ) to products,
    )
}