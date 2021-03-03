package com.socialite.solite_pos.data.source.local.entity.room.bridge

import androidx.room.*
import com.google.firebase.firestore.QuerySnapshot
import com.socialite.solite_pos.data.source.local.entity.room.master.VariantOption
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.utils.tools.RemoteUtils
import java.io.Serializable
import java.util.*

@Entity(
	tableName = OrderProductVariant.DB_NAME,
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
		Index(value = [OrderProductVariant.ID]),
		Index(value = [OrderDetail.ID]),
		Index(value = [VariantOption.ID])
	]
)
data class OrderProductVariant(
		@PrimaryKey(autoGenerate = true)
		@ColumnInfo(name = ID)
		var id: Long,

		@ColumnInfo(name = OrderDetail.ID)
		var idOrderDetail: Long,

		@ColumnInfo(name = VariantOption.ID)
		var idVariantOption: Long,

		@ColumnInfo(name = UPLOAD)
		var isUpload: Boolean
): Serializable{
	companion object: RemoteUtils<OrderProductVariant>{
		const val ID = "id_order_product_variant"

		const val DB_NAME = "order_product_variant"

		override fun toHashMap(data: OrderProductVariant): HashMap<String, Any?> {
			return hashMapOf(
					ID to data.id,
					OrderDetail.ID to data.idOrderDetail,
					VariantOption.ID to data.idVariantOption,
					UPLOAD to data.isUpload
			)
		}

		override fun toListClass(result: QuerySnapshot): List<OrderProductVariant> {
			val array: ArrayList<OrderProductVariant> = ArrayList()
			for (document in result){
				val variant = OrderProductVariant(
						document.data[ID] as Long,
						document.data[OrderDetail.ID] as Long,
						document.data[VariantOption.ID] as Long,
						document.data[UPLOAD] as Boolean
				)
				array.add(variant)
			}
			return array
		}
	}

	constructor(idOrderDetail: Long, idVariantOption: Long): this(0, idOrderDetail, idVariantOption, false)
}
