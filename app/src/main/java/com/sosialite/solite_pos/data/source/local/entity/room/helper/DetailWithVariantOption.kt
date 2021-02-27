package com.sosialite.solite_pos.data.source.local.entity.room.helper

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.OrderDetail
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.OrderProductVariant
import com.sosialite.solite_pos.data.source.local.entity.room.master.VariantOption
import java.io.Serializable

data class DetailWithVariantOption(
	@Embedded
	var detail: OrderDetail,

	@Relation(
		parentColumn = OrderDetail.ID,
		entity = VariantOption::class,
		entityColumn = VariantOption.ID,
		associateBy = Junction(
			value = OrderProductVariant::class,
			parentColumn = OrderDetail.ID,
			entityColumn = VariantOption.ID
		)
	) val options: List<VariantOption> = emptyList()
): Serializable
