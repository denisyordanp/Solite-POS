package com.socialite.solite_pos.data.source.local.entity.room.bridge

import androidx.room.*
import com.google.firebase.firestore.QuerySnapshot
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.data.source.remote.response.helper.RemoteClassUtils
import java.io.Serializable
import java.util.*

@Entity(
	tableName = OrderDetail.DB_NAME,
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
		Index(value = [OrderDetail.ID]),
		Index(value = [Order.NO]),
		Index(value = [Product.ID])
	]
)
data class OrderDetail(
		@PrimaryKey(autoGenerate = true)
		@ColumnInfo(name = ID)
		var id: Long,

		@ColumnInfo(name = Order.NO)
		var orderNo: String,

		@ColumnInfo(name = Product.ID)
		var idProduct: Long,

		@ColumnInfo(name = AMOUNT)
		var amount: Int,

		@ColumnInfo(name = UPLOAD)
		var isUpload: Boolean
): Serializable{
	companion object: RemoteClassUtils<OrderDetail> {
		const val ID = "id_order_detail"
		const val AMOUNT = "amount"

		const val DB_NAME = "order_detail"

		override fun toHashMap(data: OrderDetail): HashMap<String, Any?> {
			return hashMapOf(
					ID to data.id,
					Order.NO to data.orderNo,
					Product.ID to data.idProduct,
					AMOUNT to data.amount,
					UPLOAD to data.isUpload
			)
		}

		override fun toListClass(result: QuerySnapshot): List<OrderDetail> {
			val array: ArrayList<OrderDetail> = ArrayList()
			for (document in result){
				val detail = OrderDetail(
						document.data[ID] as Long,
						document.data[Order.NO] as String,
						document.data[Product.ID] as Long,
						(document.data[AMOUNT] as Long).toInt(),
						document.data[UPLOAD] as Boolean
				)
				array.add(detail)
			}
			return array
		}
	}

	@Ignore
	constructor(orderNo: String, idProduct: Long, amount: Int): this(0, orderNo, idProduct, amount, false)

	@Ignore
	constructor(): this(0,"", 0, 0, false)
}
