package com.socialite.solite_pos.data.schema.helper

data class BucketOrder(
    val time: Long?,
    val products: List<ProductOrderDetail>?
) {

    fun isIdle() = this == idle()

    fun getTotal() = products?.sumOf {
        val sellPrice = it.product?.price ?: 0L
        sellPrice * it.amount.toLong()
    } ?: 0

    fun productAsString(): String {
        return products?.joinToString {
            it.product?.name ?: ""
        } ?: ""
    }

    fun totalItems() = products?.sumOf { it.amount } ?: 0

    fun getProductAmount(idProduct: String): Int? {
        return products?.find {
            it.product?.id == idProduct
        }?.amount
    }

    companion object {
        fun idle() = BucketOrder(
            time = null,
            products = null
        )
    }
}
