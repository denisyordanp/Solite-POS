package com.sosialite.solite_pos.data.source.local.entity.helper

import com.sosialite.solite_pos.data.source.local.entity.Product

data class DetailOrder(
	var product: Product,
	var amount: Int
)
