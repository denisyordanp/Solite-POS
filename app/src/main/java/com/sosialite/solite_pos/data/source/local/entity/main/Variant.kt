package com.sosialite.solite_pos.data.source.local.entity.main

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.sosialite.solite_pos.data.source.local.room.AppDatabase
import java.io.Serializable

@Entity(
		tableName = AppDatabase.TBL_VARIANT,
		primaryKeys = [Variant.ID],
		indices = [
			Index(value = [Variant.ID])
		]
)
data class Variant(
		@ColumnInfo(name = ID)
		var id: Int,

		@ColumnInfo(name = NAME)
		var name: Int,

		@ColumnInfo(name = DESC)
		var desc: Int,

		@ColumnInfo(name = PRICE)
		var price: Int
) : Serializable{
	companion object{
		const val ID = "id_variant"
		const val NAME = "name"
		const val DESC = "desc"
		const val PRICE = "price"
	}
}
