package com.sosialite.solite_pos.data.source.local.entity.main

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.sosialite.solite_pos.data.source.local.room.AppDatabase
import java.io.Serializable

@Entity(
	tableName = AppDatabase.TBL_CATEGORY,
	primaryKeys = [Category.ID],
	indices = [
		Index(value = [Category.ID])
	]
)
data class Category(

	@ColumnInfo(name = ID)
	var id: Int,

	@ColumnInfo(name = NAME)
	var name: String
): Serializable{
	companion object{
		const val ID = "id_category"
		const val NAME = "name"
	}
}
