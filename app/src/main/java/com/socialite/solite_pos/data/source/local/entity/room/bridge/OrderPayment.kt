package com.socialite.solite_pos.data.source.local.entity.room.bridge

import androidx.room.*
import com.google.firebase.firestore.QuerySnapshot
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.local.entity.room.master.Payment
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.data.source.remote.response.helper.RemoteClassUtils
import java.io.Serializable
import java.util.*

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
		Index(value = [OrderPayment.ID]),
		Index(value = [Order.NO]),
		Index(value = [Payment.ID])
	]
) data class OrderPayment(
		@PrimaryKey(autoGenerate = true)
		@ColumnInfo(name = ID)
		var id: Long,

		@ColumnInfo(name = Order.NO)
		var orderNO: String,

		@ColumnInfo(name = Payment.ID)
		var idPayment: Long,

		@ColumnInfo(name = PAY)
		var pay: Long,

		@ColumnInfo(name = UPLOAD)
		var isUpload: Boolean
): Serializable{
	companion object: RemoteClassUtils<OrderPayment> {
		const val ID = "id_order_payment"
		const val PAY = "pay"

		const val DB_NAME = "order_payment"

		override fun toHashMap(data: OrderPayment): HashMap<String, Any?> {
			return hashMapOf(
					ID to data.id,
					Order.NO to data.orderNO,
					Payment.ID to data.idPayment,
					PAY to data.pay,
					UPLOAD to data.isUpload
			)
		}

		override fun toListClass(result: QuerySnapshot): List<OrderPayment> {
			val array: ArrayList<OrderPayment> = ArrayList()
			for (document in result){
				val payment = OrderPayment(
						document.data[ID] as Long,
						document.data[Order.NO] as String,
						document.data[Payment.ID] as Long,
						document.data[PAY] as Long,
						document.data[UPLOAD] as Boolean
				)
				array.add(payment)
			}
			return array
		}
	}

	@Ignore
	constructor(orderNo: String, idPayment: Long, pay: Long): this(0, orderNo, idPayment, pay, false)

	fun inReturn(total: Long): Long{
		return pay - total
	}
}
