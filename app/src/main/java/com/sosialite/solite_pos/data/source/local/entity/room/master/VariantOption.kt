package com.sosialite.solite_pos.data.source.local.entity.room.master

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import com.sosialite.solite_pos.data.source.local.room.AppDatabase
import java.io.Serializable

@Entity(
		tableName = AppDatabase.TBL_VARIANT_OPTION,
		foreignKeys = [
			ForeignKey(
					entity = Variant::class,
					parentColumns = [Variant.ID],
					childColumns = [Variant.ID],
					onDelete = ForeignKey.CASCADE)
		],
		indices = [
			Index(value = [VariantOption.ID, Variant.ID])
		]
)
data class VariantOption(

	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = ID)
	var id: Int,

	@ColumnInfo(name = Variant.ID)
	var idVariant: Int,

	@ColumnInfo(name = NAME)
	var name: String,

	@ColumnInfo(name = DESC)
	var desc: String,

	@ColumnInfo(name = PRICE)
	var price: Int,

	@ColumnInfo(name = COUNT)
	var isCount: Boolean,

	@ColumnInfo(name = STATUS)
	var isActive: Boolean
): Serializable{
	companion object{
		const val ID = "id_variant_option"
		const val STATUS = "status"
		const val PRICE = "price"
		const val COUNT = "count"
		const val DESC = "desc"
		const val NAME = "name"

		const val ALL = 2
		const val ACTIVE = 1

		fun getFilter(idVariant: Int, state: Int): SimpleSQLiteQuery {
			val query = StringBuilder().append("SELECT * FROM ")
			query.append(AppDatabase.TBL_VARIANT_OPTION)
			query.append(" WHERE ")
					.append(Variant.ID)
					.append(" = ")
					.append(idVariant)
			when(state){
				ACTIVE -> {
					query.append(" AND ")
							.append(STATUS)
							.append(" = ").append(ACTIVE)
				}
			}
			return SimpleSQLiteQuery(query.toString())
		}
	}

	constructor(idVariant: Int, name: String, desc: String, price: Int, isCount: Boolean, isActive: Boolean): this(0, idVariant, name, desc, price, isCount, isActive)
}
