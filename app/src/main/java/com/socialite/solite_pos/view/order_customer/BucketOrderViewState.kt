package com.socialite.solite_pos.view.order_customer

import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail

data class BucketOrderViewState(
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
        fun idle() = BucketOrderViewState(
            time = null,
            products = null
        )
    }
}
