package com.socialite.schema.ui.dummy

import com.socialite.schema.ui.helper.BucketOrder
import com.socialite.schema.ui.helper.ProductOrderDetail
import com.socialite.schema.ui.helper.ProductWithCategory
import com.socialite.schema.ui.main.Category
import com.socialite.schema.ui.main.Customer
import com.socialite.schema.ui.main.Product
import com.socialite.schema.ui.main.VariantOption

object DummySchema {
    val products = listOf(
        Product(
            name = "Siomay",
            desc = "Siomay ayam",
            price = 15700,
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
        Product(
            name = "Lorem ipsum dolor sit amet",
            desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
            price = 15000,
            category = "",
            image = "",
            id = "5",
            isActive = true,
            isUploaded = false
        ),
        Product(
            name = "Lorem ipsum dolor sit amet",
            desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
            price = 15000,
            category = "",
            image = "",
            id = "6",
            isActive = true,
            isUploaded = false
        ),
        Product(
            name = "Lorem ipsum dolor sit amet",
            desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
            price = 15000,
            category = "",
            image = "",
            id = "7",
            isActive = true,
            isUploaded = false
        ),
    )

    val categories = listOf(
        Category(
            id = "1",
            name = "Makanan",
            desc = "",
            isActive = true,
            isUploaded = true
        ),
        Category(
            id = "2",
            name = "Minuman",
            desc = "",
            isActive = true,
            isUploaded = true
        ),
        Category(
            id = "3",
            name = "Coffee",
            desc = "",
            isActive = true,
            isUploaded = true
        ),
        Category(
            id = "4",
            name = "Non-Coffee",
            desc = "",
            isActive = true,
            isUploaded = true
        )
    )

    val productWithCategories = listOf(
        ProductWithCategory(
            product = products[0],
            category = categories[0],
            hasVariant = false
        ),
        ProductWithCategory(
            product = products[1],
            category = categories[1],
            hasVariant = false
        ),
        ProductWithCategory(
            product = products[2],
            category = categories[2],
            hasVariant = false
        ),
        ProductWithCategory(
            product = products[3],
            category = categories[3],
            hasVariant = false
        ),
        ProductWithCategory(
            product = products[4],
            category = categories[1],
            hasVariant = false
        ),
        ProductWithCategory(
            product = products[5],
            category = categories[2],
            hasVariant = false
        ),
        ProductWithCategory(
            product = products[6],
            category = categories[3],
            hasVariant = false
        )
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

    val customers = listOf(
        Customer(
            id = "1",
            name = "Lorem",
            isUploaded = true
        ),
        Customer(
            id = "2",
            name = "Ipsum",
            isUploaded = true
        ),
        Customer(
            id = "3",
            name = "Consectetur",
            isUploaded = true
        ),
        Customer(
            id = "4",
            name = "Eiusmod",
            isUploaded = true
        ),
        Customer(
            id = "5",
            name = "Irure",
            isUploaded = true
        ),
        Customer(
            id = "6",
            name = "Voluptatem",
            isUploaded = true
        ),
        Customer(
            id = "7",
            name = "Nostrum",
            isUploaded = true
        ),
        Customer(
            id = "8",
            name = "Corporis",
            isUploaded = true
        ),
        Customer(
            id = "9",
            name = "Happiness",
            isUploaded = true
        ),
        Customer(
            id = "10",
            name = "Loves",
            isUploaded = true
        )
    )

    private val variantOptions = listOf(
        VariantOption(
            id = "1",
            variant = "1",
            name = "Pedas",
            desc = "Pedas",
            isActive = false,
            isUploaded = true
        ),
        VariantOption(
            id = "2",
            variant = "1",
            name = "Manis",
            desc = "Manis",
            isActive = false,
            isUploaded = true
        ),
        VariantOption(
            id = "3",
            variant = "1",
            name = "Pahit",
            desc = "Pahit",
            isActive = false,
            isUploaded = true
        ),
        VariantOption(
            id = "4",
            variant = "1",
            name = "Panas",
            desc = "Panas",
            isActive = false,
            isUploaded = true
        ),
        VariantOption(
            id = "5",
            variant = "1",
            name = "Dingin",
            desc = "Dingin",
            isActive = false,
            isUploaded = true
        )
    )

    val bucketOrder = BucketOrder(
        time = 1696989368742,
        products = listOf(
            ProductOrderDetail.createProduct(
                product = products[0],
                variants = variantOptions.take(2),
                amount = 2
            ),
            ProductOrderDetail.createProduct(
                product = products[1],
                variants = variantOptions.take(1),
                amount = 1
            ),
            ProductOrderDetail.createProduct(
                product = products[2],
                variants = variantOptions,
                amount = 10
            )
        )
    )
}