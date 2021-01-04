package com.sosialite.solite_pos.data.source.local.entity.bridge

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.sosialite.solite_pos.data.source.local.entity.main.Product
import com.sosialite.solite_pos.data.source.local.entity.main.Variant
import com.sosialite.solite_pos.utils.tools.helper.KeyString

@Entity(
		tableName = KeyString.Database.TBL_PRODUCT_VARIANT,
		primaryKeys = ["id"],
		foreignKeys = [
			ForeignKey(
				entity = Product::class,
				parentColumns = ["id"],
				childColumns = ["id_product"],
				onDelete = ForeignKey.CASCADE),
			ForeignKey(
					entity = Variant::class,
					parentColumns = ["id"],
					childColumns = ["id_variant"],
					onDelete = ForeignKey.CASCADE)
		],
		indices = [
			Index(value = ["id"]),
			Index(value = ["id_product"]),
			Index(value = ["id_variant"])
		]
)
data class ProductVariant(
		@ColumnInfo(name = "id")
		var id: Int,

		@ColumnInfo(name = "id_product")
		var idProduct: Int,

		@ColumnInfo(name = "id_variant")
		var idVariant: Int
)
