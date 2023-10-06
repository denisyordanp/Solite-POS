package com.socialite.schema.database.helper

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.socialite.schema.database.new_bridge.OrderDetail
import com.socialite.schema.database.new_bridge.OrderProductVariant
import com.socialite.schema.database.new_master.VariantOption
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
