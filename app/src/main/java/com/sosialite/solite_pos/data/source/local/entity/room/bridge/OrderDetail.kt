package com.sosialite.solite_pos.data.source.local.entity.room.bridge

import androidx.room.*
import com.sosialite.solite_pos.data.source.local.entity.room.master.Order
import com.sosialite.solite_pos.data.source.local.entity.room.master.Product
import com.sosialite.solite_pos.data.source.local.room.AppDatabase
import java.io.Serializable

@Entity(
	tableName = AppDatabase.TBL_ORDER_DETAIL,
	foreignKeys = [
		ForeignKey(
			entity = Order::class,
			parentColumns = [Order.NO],
			childColumns = [Order.NO],
			onDelete = ForeignKey.CASCADE),
		ForeignKey(
			entity = Product::class,
			parentColumns = [Product.ID],
			childColumns = [Product.ID],
			onDelete = ForeignKey.CASCADE)
	],
	indices = [
		Index(value = [
			OrderDetail.ID,
			Order.NO,
			Product.ID
		])
	]
)
data class OrderDetail(
		@PrimaryKey(autoGenerate = true)
		@ColumnInfo(name = ID)
		var id: Int,

		@ColumnInfo(name = Order.NO)
		var orderNo: String,

		@ColumnInfo(name = Product.ID)
		var idProduct: Int,

		@ColumnInfo(name = AMOUNT)
		var amount: Int
): Serializable{
	companion object{
		const val ID = "id_order_detail"
		const val AMOUNT = "amount"
	}

	constructor(orderNo: String, idProduct: Int, amount: Int): this(0, orderNo, idProduct, amount)
	constructor(): this("", 0, 0)
}
