package com.sosialite.solite_pos.data.source.local.entity.room.bridge

import androidx.room.*
import com.sosialite.solite_pos.data.source.local.entity.room.master.VariantOption
import com.sosialite.solite_pos.data.source.local.room.AppDatabase
import java.io.Serializable

@Entity(
	tableName = AppDatabase.TBL_ORDER_PRODUCT_VARIANT,
	foreignKeys = [
		ForeignKey(
			entity = OrderDetail::class,
			parentColumns = [OrderDetail.ID],
			childColumns = [OrderDetail.ID],
			onDelete = ForeignKey.CASCADE),
		ForeignKey(
			entity = VariantOption::class,
			parentColumns = [VariantOption.ID],
			childColumns = [VariantOption.ID],
			onDelete = ForeignKey.CASCADE)
	],
	indices = [
		Index(value = [
			OrderProductVariant.ID,
			OrderDetail.ID,
			VariantOption.ID
		])
	]
)
data class OrderProductVariant(
		@PrimaryKey(autoGenerate = true)
		@ColumnInfo(name = ID)
		var id: Int,

		@ColumnInfo(name = OrderDetail.ID)
		var idOrderDetail: Int,

		@ColumnInfo(name = VariantOption.ID)
		var idVariantOption: Int,

		@ColumnInfo(name = AMOUNT)
		var amount: Int
): Serializable{
	companion object{
		const val ID = "id_order_product_variant"
		const val AMOUNT = "amount"
	}

	constructor(idOrderDetail: Int, idVariantOption: Int, amount: Int): this(0, idOrderDetail, idVariantOption, amount)
}
