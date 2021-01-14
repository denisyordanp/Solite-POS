package com.sosialite.solite_pos.data.source.local.entity.room.bridge

import androidx.room.*
import com.sosialite.solite_pos.data.source.local.entity.room.master.Order
import com.sosialite.solite_pos.data.source.local.entity.room.master.Product
import com.sosialite.solite_pos.data.source.local.room.AppDatabase
import java.io.Serializable

@Entity(
	tableName = AppDatabase.TBL_ORDER_PRODUCT_VARIANT_MIX,
	foreignKeys = [
		ForeignKey(
			entity = OrderDetail::class,
			parentColumns = [OrderDetail.ID],
			childColumns = [OrderDetail.ID],
			onDelete = ForeignKey.CASCADE),
		ForeignKey(
			entity = VariantMix::class,
			parentColumns = [VariantMix.ID],
			childColumns = [VariantMix.ID],
			onDelete = ForeignKey.CASCADE)
	],
	indices = [
		Index(value = [OrderProductVariantMix.ID]),
		Index(value = [OrderDetail.ID]),
		Index(value = [VariantMix.ID]),
	]
)
data class OrderProductVariantMix(
		@PrimaryKey(autoGenerate = true)
		@ColumnInfo(name = ID)
		var id: Int,

		@ColumnInfo(name = OrderDetail.ID)
		var idOrderDetail: Int,

		@ColumnInfo(name = VariantMix.ID)
		var idVariantMix: Int,

		@ColumnInfo(name = AMOUNT)
		var amount: Int
): Serializable{
	companion object{
		const val ID = "id_order_product_variant_mix"
		const val AMOUNT = "amount"
	}

	constructor(idOrderDetail: Int, idVariantMix: Int, amount: Int): this(0, idOrderDetail, idVariantMix, amount)
}
