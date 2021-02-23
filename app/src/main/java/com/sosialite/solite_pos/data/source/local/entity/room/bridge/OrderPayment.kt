package com.sosialite.solite_pos.data.source.local.entity.room.bridge

import androidx.room.*
import com.sosialite.solite_pos.data.source.local.entity.room.master.Order
import com.sosialite.solite_pos.data.source.local.entity.room.master.Payment
import java.io.Serializable

@Entity(
	tableName = OrderPayment.DB_NAME,
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
		Index(value = [
			OrderPayment.ID,
			Order.NO,
			Payment.ID
		])
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

		const val DB_NAME = "order_payment"
	}

	constructor(orderNo: String, idPayment: Int, pay: Int): this(0, orderNo, idPayment, pay)

	fun inReturn(total: Int): Int{
		return pay - total
	}

	val hashMap: HashMap<String, Any?>
		get() {
			return hashMapOf(
				ID to id,
				Order.NO to orderNO,
				Payment.ID to idPayment,
				PAY to pay
			)
		}
}
