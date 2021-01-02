package com.sosialite.solite_pos.data.source.local.entity.helper

import com.sosialite.solite_pos.data.source.local.entity.Product
import java.io.Serializable

data class DetailOrder(
	var product: Product?,
	var amount: Int
): Serializable{
	constructor(): this(null, 0)
}
