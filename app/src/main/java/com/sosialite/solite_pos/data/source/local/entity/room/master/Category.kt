package com.sosialite.solite_pos.data.source.local.entity.room.master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.sqlite.db.SimpleSQLiteQuery
import com.sosialite.solite_pos.data.source.local.room.AppDatabase
import java.io.Serializable

@Entity(
	tableName = AppDatabase.TBL_CATEGORY,
	indices = [
		Index(value = [Category.ID])
	]
)
data class Category(

	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = ID)
	var id: Int,

	@ColumnInfo(name = NAME)
	var name: String,

	@ColumnInfo(name = DESC)
	var desc: String,

	@ColumnInfo(name = STOCK)
	var isStock: Boolean,

	@ColumnInfo(name = STATUS)
	var isActive: Boolean
): Serializable{
	companion object{
		const val ID = "id_category"
		const val STATUS = "status"
		const val STOCK = "stock"
		const val NAME = "name"
		const val DESC = "desc"

		const val ALL = 2
		const val ACTIVE = 1

		fun getFilter(state: Int): SimpleSQLiteQuery {
			val query = StringBuilder().append("SELECT * FROM ")
			query.append(AppDatabase.TBL_CATEGORY)
			when(state){
				ACTIVE -> {
					query.append(" WHERE ")
						.append(STATUS)
						.append(" = ").append(ACTIVE)
				}
			}
			return SimpleSQLiteQuery(query.toString())
		}
	}

	constructor(name: String, desc: String, isStock: Boolean, isActive: Boolean): this(0, name, desc, isStock, isActive)
}
