package com.socialite.solite_pos.data.source.local.entity.room.master

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import com.google.firebase.firestore.QuerySnapshot
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.data.source.remote.response.helper.RemoteClassUtils
import java.io.Serializable
import java.util.*

@Entity(
		tableName = VariantOption.DB_NAME,
		foreignKeys = [
			ForeignKey(
					entity = Variant::class,
					parentColumns = [Variant.ID],
					childColumns = [Variant.ID],
					onDelete = ForeignKey.CASCADE)
		],
		indices = [
			Index(value = [VariantOption.ID]),
			Index(value = [Variant.ID])
		]
)
data class VariantOption(

		@PrimaryKey(autoGenerate = true)
		@ColumnInfo(name = ID)
		var id: Long,

		@ColumnInfo(name = Variant.ID)
		var idVariant: Long,

		@ColumnInfo(name = NAME)
		var name: String,

		@ColumnInfo(name = DESC)
		var desc: String,

		@ColumnInfo(name = COUNT)
		var isCount: Boolean,

		@ColumnInfo(name = STATUS)
		var isActive: Boolean,

		@ColumnInfo(name = UPLOAD)
		var isUploaded: Boolean
): Serializable{
	companion object: RemoteClassUtils<VariantOption> {
		const val ID = "id_variant_option"
		const val STATUS = "status"
		const val COUNT = "count"
		const val DESC = "desc"
		const val NAME = "name"

		const val ALL = 2
		const val ACTIVE = 1

		const val DB_NAME = "variant_option"

		fun getFilter(idVariant: Long, state: Int): SimpleSQLiteQuery {
			val query = StringBuilder().append("SELECT * FROM ")
			query.append(DB_NAME)
			query.append(" WHERE ")
					.append(Variant.ID)
					.append(" = ")
					.append(idVariant)
			if (state == ACTIVE){
				query.append(" AND ")
						.append(STATUS)
						.append(" = ").append(ACTIVE)
			}
			return SimpleSQLiteQuery(query.toString())
		}

		override fun toHashMap(data: VariantOption): HashMap<String, Any?> {
			return hashMapOf(
					ID to data.id,
					Variant.ID to data.idVariant,
					NAME to data.name,
					DESC to data.desc,
					COUNT to data.isCount,
					STATUS to data.isActive,
					UPLOAD to data.isUploaded
			)
		}

		override fun toListClass(result: QuerySnapshot): List<VariantOption> {
			val array: ArrayList<VariantOption> = ArrayList()
			for (document in result){
				val supplier = VariantOption(
						document.data[ID] as Long,
						document.data[Variant.ID] as Long,
						document.data[NAME] as String,
						document.data[DESC] as String,
						document.data[COUNT] as Boolean,
						document.data[STATUS] as Boolean,
						document.data[UPLOAD] as Boolean
				)
				array.add(supplier)
			}
			return array
		}
	}

	constructor(id: Long, idVariant: Long, name: String, desc: String, isCount: Boolean, isActive: Boolean): this(id, idVariant, name, desc, isCount, isActive, false)
	constructor(idVariant: Long, name: String, desc: String, isCount: Boolean, isActive: Boolean): this(0, idVariant, name, desc, isCount, isActive, false)
}
