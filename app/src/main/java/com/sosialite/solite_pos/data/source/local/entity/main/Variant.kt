package com.sosialite.solite_pos.data.source.local.entity.main

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.sosialite.solite_pos.utils.tools.helper.KeyString
import java.io.Serializable

@Entity(
		tableName = KeyString.Database.TBL_VARIANT,
		primaryKeys = ["id"],
		indices = [
			Index(value = ["id"])
		]
)
data class Variant(
		@ColumnInfo(name = "id")
		var id: Int,

		@ColumnInfo(name = "name")
		var name: Int,

		@ColumnInfo(name = "desc")
		var desc: Int,

		@ColumnInfo(name = "price")
		var price: Int
) : Serializable
