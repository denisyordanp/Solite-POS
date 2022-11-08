package com.socialite.solite_pos.data.source.local.entity.helper

import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.data.source.local.entity.room.master.VariantOption
import java.io.Serializable

data class ProductOrderDetail(
    var product: Product?,
    var variants: List<VariantOption>,
    var mixProducts: List<ProductMixOrderDetail>,
    var amount: Int,
    var type: Int?
) : Serializable {
    constructor(
        product: Product?,
        variants: List<VariantOption>,
        mixVariants: List<ProductMixOrderDetail>,
        amount: Int
    ) : this(product, variants, mixVariants, amount, null)

    constructor(type: Int) : this(null, listOf(), listOf(), 0, type)

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

    fun totalPrice() = (product?.sellPrice ?: 0) * amount

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
            return ProductOrderDetail(product, variants, listOf(), amount)
        }

        fun createMix(
            product: Product?,
            mixes: List<ProductMixOrderDetail>,
            amount: Int
        ): ProductOrderDetail {
            return ProductOrderDetail(product, listOf(), mixes, amount)
        }

        fun productNoVariant(product: Product) = ProductOrderDetail(
            product = product,
            variants = listOf(),
            mixVariants = listOf(),
            amount = 1,
        )

        fun empty() = ProductOrderDetail(
            product = null,
            variants = listOf(),
            mixProducts = listOf(),
            amount = 0,
            type = null
        )
    }
}
