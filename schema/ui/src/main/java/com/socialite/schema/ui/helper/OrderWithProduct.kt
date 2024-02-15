package com.socialite.schema.ui.helper

import java.io.Serializable

data class OrderWithProduct(
    val orderData: OrderData,
    val products: List<ProductOrderDetail>
): Serializable {

	val grandTotal: Long
		get() {
			return products.sumOf {
				it.product?.let { pr ->
					pr.price * it.amount
				} ?: 0
			}
		}

	val totalPromo: Long
		get() = orderData.orderPromo?.totalPromo ?: 0L

	val grandTotalWithPromo: Long
		get() { return grandTotal - totalPromo}

	val totalItem: Int
	get() {
		return products.sumOf {
			it.amount
		}
	}
}
