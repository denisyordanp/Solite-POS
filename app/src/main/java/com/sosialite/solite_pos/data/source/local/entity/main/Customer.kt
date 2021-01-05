package com.sosialite.solite_pos.data.source.local.entity.main

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.sosialite.solite_pos.data.source.local.room.AppDatabase
import java.io.Serializable

@Entity(
	tableName = AppDatabase.TBL_CUSTOMER,
	primaryKeys = [Customer.ID],
	indices = [
		Index(value = [Customer.ID]),
	]
)
data class Customer(
	@ColumnInfo(name = ID)
	var id: Int,
	@ColumnInfo(name = NAME)
	var name: String
): Serializable{
	companion object{
		const val ID_ADD = -1

		const val ID = "id_customer"
		const val NAME = "name"
	}
}
