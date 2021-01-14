package com.sosialite.solite_pos.data.source.local.entity.room.bridge

import androidx.room.*
import com.sosialite.solite_pos.data.source.local.entity.room.master.Product
import com.sosialite.solite_pos.data.source.local.entity.room.master.Variant
import com.sosialite.solite_pos.data.source.local.entity.room.master.VariantOption
import com.sosialite.solite_pos.data.source.local.room.AppDatabase
import java.io.Serializable

@Entity(
		tableName = AppDatabase.TBL_VARIANT_PRODUCT,
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
			Index(value = [VariantOption.ID]),
			Index(value = [Product.ID]),
		]
)
data class VariantProduct(

	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = ID)
	var id: Int,

	@ColumnInfo(name = Variant.ID)
	var idVariant: Int,

	@ColumnInfo(name = VariantOption.ID)
	var idVariantOption: Int,

	@ColumnInfo(name = Product.ID)
	var idProduct: Int
): Serializable{
	companion object{
		const val ID = "id_variant_product"
	}

	constructor(idVariant: Int, idVariantOption: Int, idProduct: Int): this(0, idVariant, idVariantOption, idProduct)
}
