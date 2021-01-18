package com.sosialite.solite_pos.data.source.local.entity.room.bridge

import androidx.room.*
import com.sosialite.solite_pos.data.source.local.entity.room.master.Order
import com.sosialite.solite_pos.data.source.local.entity.room.master.Payment
import com.sosialite.solite_pos.data.source.local.room.AppDatabase
import java.io.Serializable

@Entity(
	tableName = AppDatabase.TBL_ORDER_PAYMENT,
	foreignKeys = [
		ForeignKey(
			entity = Order::class,
			parentColumns = [Order.NO],
			childColumns = [Order.NO],
			onDelete = ForeignKey.CASCADE),
		ForeignKey(
			entity = Payment::class,
			parentColumns = [Payment.ID],
			childColumns = [Payment.ID],
			onDelete = ForeignKey.CASCADE)
	],
	indices = [
		Index(value = [OrderPayment.ID]),
		Index(value = [Order.NO]),
		Index(value = [Payment.ID]),
	]
) data class OrderPayment(
		@PrimaryKey(autoGenerate = true)
		@ColumnInfo(name = ID)
		var id: Int,

		@ColumnInfo(name = Order.NO)
		var orderNO: String,

		@ColumnInfo(name = Payment.ID)
		var idPayment: Int,

		@ColumnInfo(name = PAY)
		var pay: Int
): Serializable{
	companion object{
		const val ID = "id_order_payment"
		const val PAY = "pay"
	}

	constructor(orderNo: String, idPayment: Int, pay: Int): this(0, orderNo, idPayment, pay)

	fun inReturn(total: Int): Int{
		return total - pay
	}
}
