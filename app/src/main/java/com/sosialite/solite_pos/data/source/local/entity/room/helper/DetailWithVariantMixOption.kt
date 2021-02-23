package com.sosialite.solite_pos.data.source.local.entity.room.helper

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.OrderDetail
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.OrderProductVariant
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.OrderProductVariantMix
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.VariantProduct
import com.sosialite.solite_pos.data.source.local.entity.room.master.*
import java.io.Serializable

data class DetailWithVariantMixOption(
		@Embedded
		var detail: OrderDetail,

		@Relation(parentColumn = OrderDetail.ID, entityColumn = OrderDetail.ID)
		var variantsMix: List<OrderProductVariantMix> = emptyList()
): Serializable
