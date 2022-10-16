package com.socialite.solite_pos.data.source.local.entity.helper

import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.data.source.local.entity.room.master.VariantOption
import java.io.Serializable

data class ProductOrderDetail(
    var product: Product?,
    var variants: ArrayList<VariantOption>,
    var mixProducts: ArrayList<ProductMixOrderDetail>,
    var amount: Int,
    var type: Int?
) : Serializable {
    constructor(
        product: Product?,
        variants: ArrayList<VariantOption>,
        mixVariants: ArrayList<ProductMixOrderDetail>,
        amount: Int
    ) : this(product, variants, mixVariants, amount, null)

    constructor(type: Int) : this(null, ArrayList(), ArrayList(), 0, type)

    fun generateVariantsString(): String {
        return if (variants.isEmpty()) {
            product?.desc ?: ""
        } else {
            variants.joinToString {
                it.name
            }
        }
    }

    fun addOption(option: VariantOption, prevOption: VariantOption? = null) = variants.apply {
        prevOption?.let {
            remove(it)
        }
        add(option)
    }

    fun removeOption(option: VariantOption) = variants.apply {
        val existingOption = variants.find { it == option }
        if (existingOption != null) {
            remove(option)
        }
    }

    fun totalPrice() = (product?.sellPrice ?: 0) * amount

    companion object {
        const val GRAND_TOTAL = 1
        const val PAYMENT = 2
        const val RETURN = 3
        const val TITLE = 4

        fun createProduct(
            product: Product?,
            variants: ArrayList<VariantOption>,
            amount: Int
        ): ProductOrderDetail {
            return ProductOrderDetail(product, variants, ArrayList(), amount)
        }

        fun createMix(
            product: Product?,
            mixes: ArrayList<ProductMixOrderDetail>,
            amount: Int
        ): ProductOrderDetail {
            return ProductOrderDetail(product, ArrayList(), mixes, amount)
        }

        fun productNoVariant(product: Product) = ProductOrderDetail(
			product = product,
			variants = arrayListOf(),
			mixVariants = arrayListOf(),
			amount = 1,
        )

        fun empty() = ProductOrderDetail(
            product = null,
            variants = arrayListOf(),
            mixProducts = arrayListOf(),
            amount = 0,
            type = null
        )

        val grand: ProductOrderDetail
            get() = ProductOrderDetail(GRAND_TOTAL)

        val payment: ProductOrderDetail
            get() = ProductOrderDetail(PAYMENT)

        val payReturn: ProductOrderDetail
            get() = ProductOrderDetail(RETURN)

        val title: ProductOrderDetail
            get() = ProductOrderDetail(TITLE)
    }
}
