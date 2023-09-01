package com.socialite.domain.schema

import com.socialite.domain.schema.main.Product
import com.socialite.domain.schema.main.VariantOption
import java.io.Serializable

data class ProductOrderDetail(
    val product: Product?,
    val variants: List<VariantOption>,
    val amount: Int
) : Serializable {

    fun generateVariantsString(): String {
        return if (variants.isEmpty()) {
            product?.desc ?: ""
        } else {
            variants.joinToString {
                it.name
            }
        }
    }

    fun addOption(option: VariantOption, prevOption: VariantOption? = null) =
        variants.toMutableList().apply {
            prevOption?.let {
                remove(it)
            }
            add(option)
        }

    fun removeOption(option: VariantOption) = variants.toMutableList().apply {
        val existingOption = variants.find { it == option }
        if (existingOption != null) {
            remove(option)
        }
    }

    fun totalPrice() = (product?.price ?: 0) * amount

    fun isAllMustVariantSelected(baseVariants: List<VariantWithOptions>): Boolean {
        val mustVariants = baseVariants.filter { it.variant.isMust == true && it.isOptionAvailable() }
        return if (mustVariants.isNotEmpty()) {
            mustVariants.none { v ->
                val newList = v.options + variants
                newList.groupBy { it.id }
                    .filter { it.value.size > 1 }
                    .isEmpty()
            }
        } else {
            true
        }
    }

    companion object {

        fun createProduct(
            product: Product?,
            variants: List<VariantOption>,
            amount: Int
        ): ProductOrderDetail {
            return ProductOrderDetail(product, variants, amount)
        }

        fun productNoVariant(product: Product) = ProductOrderDetail(
            product = product,
            variants = listOf(),
            amount = 1,
        )

        fun empty() = ProductOrderDetail(
            product = null,
            variants = listOf(),
            amount = 0
        )
    }
}

fun List<ProductOrderDetail>.findExisting(compare: ProductOrderDetail): ProductOrderDetail? {
    return this.find {
        it.product == compare.product &&
                it.variants == compare.variants
    }
}
