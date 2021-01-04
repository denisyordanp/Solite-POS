package com.sosialite.solite_pos.data.source.local.entity.main

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.sosialite.solite_pos.utils.tools.helper.KeyString

@Entity(
	tableName = KeyString.Database.TBL_CATEGORY,
	primaryKeys = ["id"],
	indices = [
		Index(value = ["id"])
	]
)
data class Category(
	@ColumnInfo(name = "id")
	var id: Int,
	@ColumnInfo(name = "name")
	var name: String
)
