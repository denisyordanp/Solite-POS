package com.sosialite.solite_pos.data.source.local.entity.helper

import com.sosialite.solite_pos.data.source.local.entity.room.master.Product
import com.sosialite.solite_pos.data.source.local.entity.room.master.VariantOption
import java.io.Serializable

data class ProductOrderDetail(
		var product: Product?,
		var variants: ArrayList<VariantOption>,
		var amount: Int,
		var type: Int?
): Serializable{
	constructor(product: Product?, variants: ArrayList<VariantOption>, amount: Int): this(product, variants, amount, null)
	constructor(type: Int): this(null, ArrayList(), 0, type)

	companion object{
		const val GRAND_TOTAL = 1
		const val PAYMENT = 2
		const val RETURN = 3
		const val TITLE = 4

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
