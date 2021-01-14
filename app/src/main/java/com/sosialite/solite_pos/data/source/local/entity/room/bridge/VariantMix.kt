package com.sosialite.solite_pos.data.source.local.entity.room.bridge

import androidx.room.*
import com.sosialite.solite_pos.data.source.local.entity.room.master.Product
import com.sosialite.solite_pos.data.source.local.entity.room.master.Variant
import com.sosialite.solite_pos.data.source.local.room.AppDatabase
import java.io.Serializable

@Entity(
		tableName = AppDatabase.TBL_VARIANT_MIX,
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
			Index(value = [Product.ID]),
		]
)
data class VariantMix(

	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = ID)
	var id: Int,

	@ColumnInfo(name = Variant.ID)
	var idVariant: Int,

	@ColumnInfo(name = Product.ID)
	var idProduct: Int
): Serializable{
	companion object{
		const val ID = "id_variant_mix"
	}

	constructor(idVariant: Int, idProduct: Int): this(0, idVariant, idProduct)
}
