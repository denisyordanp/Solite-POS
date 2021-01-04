package com.sosialite.solite_pos.data.source.local.entity.main

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.sosialite.solite_pos.utils.tools.helper.KeyString
import java.io.Serializable

@Entity(
	tableName = KeyString.Database.TBL_CUSTOMER,
	primaryKeys = ["id"],
	indices = [
		Index(value = ["id"]),
	]
)
data class Customer(
	@ColumnInfo(name = "id")
	var id: Int,
	@ColumnInfo(name = "name")
	var name: String
): Serializable{
	companion object{
		const val ID_ADD = -1
	}
}
