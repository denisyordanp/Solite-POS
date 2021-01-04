package com.sosialite.solite_pos.data.source.local.entity.main

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import com.sosialite.solite_pos.utils.tools.helper.KeyString
import java.io.Serializable

@Entity(
		tableName = KeyString.Database.TBL_PRODUCT,
		primaryKeys = ["id"],
		foreignKeys = [ForeignKey(
				entity = Category::class,
				parentColumns = ["id"],
				childColumns = ["category"],
				onDelete = CASCADE)
		],
		indices = [
			Index(value = ["code"]),
			Index(value = ["category"])
		]
)
data class Product(
		@ColumnInfo(name = "id")
		var id: Int,

		@ColumnInfo(name = "name")
		var name: String,

		@ColumnInfo(name = "category")
		var category: Int,

		@ColumnInfo(name = "desc")
		var desc: String,

		@ColumnInfo(name = "price")
		var price: Int,

		@ColumnInfo(name = "stock")
		var stock: Int
): Serializable
