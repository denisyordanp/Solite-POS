package com.socialite.solite_pos.data.source.local.entity.helper

data class BucketOrder(
    val time: Long?,
    val products: List<ProductOrderDetail>?
) {

    fun isIdle() = this == idle()

    fun getTotal() = products?.sumOf {
        val sellPrice = it.product?.sellPrice ?: 0L
        sellPrice * it.amount.toLong()
    } ?: 0

    fun productAsString(): String {
        return products?.joinToString {
            it.product?.name ?: ""
        } ?: ""
    }

    fun totalItems() = products?.count() ?: 0

    companion object {
        fun idle() = BucketOrder(
            time = null,
            products = null
        )
    }
}
