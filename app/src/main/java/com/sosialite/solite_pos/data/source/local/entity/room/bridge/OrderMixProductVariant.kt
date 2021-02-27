package com.sosialite.solite_pos.data.source.local.entity.room.bridge

import androidx.room.*
import com.google.firebase.firestore.QuerySnapshot
import com.sosialite.solite_pos.data.source.local.entity.room.master.VariantOption
import com.sosialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import com.sosialite.solite_pos.utils.tools.RemoteUtils
import java.io.Serializable
import java.util.*

@Entity(
		tableName = OrderMixProductVariant.DB_NAME,
		foreignKeys = [
			ForeignKey(
					entity = OrderProductVariantMix::class,
					parentColumns = [OrderProductVariantMix.ID],
					childColumns = [OrderProductVariantMix.ID],
					onDelete = ForeignKey.CASCADE),
			ForeignKey(
					entity = VariantOption::class,
					parentColumns = [VariantOption.ID],
					childColumns = [VariantOption.ID],
					onDelete = ForeignKey.CASCADE)
		],
		indices = [
			Index(value = [OrderMixProductVariant.ID]),
			Index(value = [OrderProductVariantMix.ID]),
			Index(value = [VariantOption.ID])
		]
)
data class OrderMixProductVariant(
		@PrimaryKey(autoGenerate = true)
		@ColumnInfo(name = ID)
		var id: Long,

		@ColumnInfo(name = OrderProductVariantMix.ID)
		var idOrderProductMixVariant: Long,

		@ColumnInfo(name = VariantOption.ID)
		var idVariantOption: Long,

		@ColumnInfo(name = UPLOAD)
		var isUpload: Boolean
): Serializable{
	companion object: RemoteUtils<OrderMixProductVariant>{
		const val ID = "id_order_mix_product_variant"

		const val DB_NAME = "order_mix_product_variant"

		override fun toHashMap(data: OrderMixProductVariant): HashMap<String, Any?> {
			return hashMapOf(
					ID to data.id,
					OrderProductVariantMix.ID to data.idOrderProductMixVariant,
					VariantOption.ID to data.idVariantOption,
					UPLOAD to data.isUpload
			)
		}

		override fun toListClass(result: QuerySnapshot): List<OrderMixProductVariant> {
			val array: ArrayList<OrderMixProductVariant> = ArrayList()
			for (document in result){
				val mix = OrderMixProductVariant(
						document.data[ID] as Long,
						document.data[OrderProductVariantMix.ID] as Long,
						document.data[VariantOption.ID] as Long,
						document.data[UPLOAD] as Boolean
				)
				array.add(mix)
			}
			return array
		}
	}

	constructor(idOrderProductMixVariant: Long, idVariantOption: Long): this(0, idOrderProductMixVariant, idVariantOption, false)
}
