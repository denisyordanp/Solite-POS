package com.sosialite.solite_pos.data.source.local.entity.bridge

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.sosialite.solite_pos.data.source.local.entity.bridge.ProductVariant.Companion.ID
import com.sosialite.solite_pos.data.source.local.entity.main.Product
import com.sosialite.solite_pos.data.source.local.entity.main.Variant
import com.sosialite.solite_pos.data.source.local.room.AppDatabase
import java.io.Serializable

@Entity(
		tableName = AppDatabase.TBL_PRODUCT_VARIANT,
		primaryKeys = [ID],
		foreignKeys = [
			ForeignKey(
				entity = Product::class,
				parentColumns = [Product.ID],
				childColumns = [Product.ID],
				onDelete = ForeignKey.CASCADE),
			ForeignKey(
					entity = Variant::class,
					parentColumns = [Variant.ID],
					childColumns = [Variant.ID],
					onDelete = ForeignKey.CASCADE)
		],
		indices = [
			Index(value = [ID]),
			Index(value = [Product.ID]),
			Index(value = [Variant.ID])
		]
)
data class ProductVariant(
		@ColumnInfo(name = ID)
		var id: Int,

		@ColumnInfo(name = Product.ID)
		var idProduct: Int,

		@ColumnInfo(name = Variant.ID)
		var idVariant: Int
): Serializable{
	companion object{
		const val ID = "id_product_variant"
	}
}
