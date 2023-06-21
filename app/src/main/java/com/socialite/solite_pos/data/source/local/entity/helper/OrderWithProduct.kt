package com.socialite.solite_pos.data.source.local.entity.helper

import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import java.io.Serializable

data class OrderWithProduct(
		val order: OrderData,
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
		get() = order.orderPromo?.totalPromo ?: 0L

	val grandTotalWithPromo: Long
		get() { return grandTotal - totalPromo}

	val totalItem: Int
	get() {
		return products.sumOf {
			it.amount
		}
	}
}
