package com.socialite.solite_pos.data.source.local.entity.room.bridge

import androidx.room.*
import com.google.firebase.firestore.QuerySnapshot
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.data.source.local.entity.room.master.Variant
import com.socialite.solite_pos.data.source.local.entity.room.master.VariantOption
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.data.source.remote.response.helper.RemoteClassUtils
import java.io.Serializable
import java.util.*

@Entity(
		tableName = VariantProduct.DB_NAME,
		foreignKeys = [
			ForeignKey(
					entity = VariantOption::class,
					parentColumns = [VariantOption.ID],
					childColumns = [VariantOption.ID],
					onDelete = ForeignKey.CASCADE
			),
			ForeignKey(
					entity = Variant::class,
					parentColumns = [Variant.ID],
					childColumns = [Variant.ID],
					onDelete = ForeignKey.CASCADE
			),
			ForeignKey(
					entity = Product::class,
					parentColumns = [Product.ID],
					childColumns = [Product.ID],
					onDelete = ForeignKey.CASCADE
			)
		],
		indices = [
			Index(value = [VariantProduct.ID]),
			Index(value = [Variant.ID]),
			Index(value = [VariantOption.ID]),
			Index(value = [Product.ID])
		]
)
data class VariantProduct(

	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = ID)
	var id: Long,

	@ColumnInfo(name = AppDatabase.REPLACED_UUID, defaultValue = "")
	val new_id: String,

	@ColumnInfo(name = Variant.ID)
	var idVariant: Long,

	@ColumnInfo(name = VariantOption.ID)
	var idVariantOption: Long,

	@ColumnInfo(name = Product.ID)
	var idProduct: Long,

	@ColumnInfo(name = UPLOAD)
	var isUploaded: Boolean
): Serializable{
	companion object: RemoteClassUtils<VariantProduct> {
		const val ID = "id_variant_product"

		const val DB_NAME = "variant_product"

		override fun toHashMap(data: VariantProduct): HashMap<String, Any?> {
			return hashMapOf(
					ID to data.id,
					Variant.ID to data.idVariant,
					VariantOption.ID to data.idVariantOption,
					Product.ID to data.idProduct,
					UPLOAD to data.isUploaded
			)
		}

		override fun toListClass(result: QuerySnapshot): List<VariantProduct> {
			val array: ArrayList<VariantProduct> = ArrayList()
			for (document in result){
				val variant = VariantProduct(
						document.data[ID] as Long,
						document.data[AppDatabase.REPLACED_UUID] as String,
						document.data[Variant.ID] as Long,
						document.data[VariantOption.ID] as Long,
						document.data[Product.ID] as Long,
						document.data[UPLOAD] as Boolean
				)
				array.add(variant)
			}
			return array
		}
	}

	@Ignore
	constructor(id: Long, idVariant: Long, idVariantOption: Long, idProduct: Long): this(id, UUID.randomUUID().toString(), idVariant, idVariantOption, idProduct, false)

	@Ignore
	constructor(idVariant: Long, idVariantOption: Long, idProduct: Long): this(0, UUID.randomUUID().toString(), idVariant, idVariantOption, idProduct, false)
}
