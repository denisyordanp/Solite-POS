package com.socialite.solite_pos.data.source.local.entity.room.bridge

import androidx.room.*
import com.google.firebase.firestore.QuerySnapshot
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.data.source.local.entity.room.master.Variant
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.utils.tools.RemoteUtils
import java.io.Serializable
import java.util.*

@Entity(
		tableName = VariantMix.DB_NAME,
		foreignKeys = [
			ForeignKey(
					entity = Variant::class,
					parentColumns = [Variant.ID],
					childColumns = [Variant.ID],
					onDelete = ForeignKey.CASCADE),
			ForeignKey(
					entity = Product::class,
					parentColumns = [Product.ID],
					childColumns = [Product.ID],
					onDelete = ForeignKey.CASCADE)
		],
		indices = [
			Index(value = [VariantMix.ID]),
			Index(value = [Variant.ID]),
			Index(value = [Product.ID])
		]
)
data class VariantMix(

	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = ID)
	var id: Long,

	@ColumnInfo(name = Variant.ID)
	var idVariant: Long,

	@ColumnInfo(name = Product.ID)
	var idProduct: Long,

	@ColumnInfo(name = UPLOAD)
	var isUploaded: Boolean
): Serializable{
	companion object: RemoteUtils<VariantMix>{
		const val ID = "id_variant_mix"

		const val DB_NAME = "variant_mix"

		override fun toHashMap(data: VariantMix): HashMap<String, Any?> {
			return hashMapOf(
					ID to data.id,
					Variant.ID to data.idVariant,
					Product.ID to data.idProduct,
					UPLOAD to data.isUploaded
			)
		}

		override fun toListClass(result: QuerySnapshot): List<VariantMix> {
			val array: ArrayList<VariantMix> = ArrayList()
			for (document in result){
				val mix = VariantMix(
						document.data[ID] as Long,
						document.data[Variant.ID] as Long,
						document.data[Product.ID] as Long,
						document.data[UPLOAD] as Boolean
				)
				array.add(mix)
			}
			return array
		}
	}

	constructor(id: Long, idVariant: Long, idProduct: Long): this(id, idVariant, idProduct, false)
	constructor(idVariant: Long, idProduct: Long): this(0, idVariant, idProduct, false)
}
