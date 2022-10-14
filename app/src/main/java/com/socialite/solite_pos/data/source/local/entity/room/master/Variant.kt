package com.socialite.solite_pos.data.source.local.entity.room.master

import androidx.room.*
import com.google.firebase.firestore.QuerySnapshot
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.data.source.remote.response.helper.RemoteClassUtils
import java.io.Serializable
import java.util.*

@Entity(
		tableName = Variant.DB_NAME,
		indices = [
			Index(value = [Variant.ID])
		]
)
data class Variant(
		@ColumnInfo(name = NAME)
		var name: String,

		@ColumnInfo(name = TYPE)
		var type: Int,

		@ColumnInfo(name = MUST)
		var isMust: Boolean? = null,

		@ColumnInfo(name = MIX)
		var isMix: Boolean,

		@ColumnInfo(name = UPLOAD)
		var isUploaded: Boolean
) : Serializable{

	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = ID)
	var id: Long = 0

	fun isSingleOption() = type == ONE_OPTION

	companion object: RemoteClassUtils<Variant> {
		const val ID = "id_variant"
		const val NAME = "name"
		const val TYPE = "type"
		const val MUST = "must"
		const val MIX = "mix"

		const val DB_NAME = "variant"

		const val ONE_OPTION = 1
		const val MULTIPLE_OPTION = 2

		override fun toHashMap(data: Variant): HashMap<String, Any?> {
			return hashMapOf(
					ID to data.id,
					NAME to data.name,
					TYPE to data.type,
					MUST to data.isMust,
					MIX to data.isMix,
					UPLOAD to data.isUploaded
			)
		}

		override fun toListClass(result: QuerySnapshot): List<Variant> {
			val array: ArrayList<Variant> = ArrayList()
			for (document in result){
				val variant = Variant(
						document.data[ID] as Long,
						document.data[NAME] as String,
						(document.data[TYPE] as Long).toInt(),
						document.data[MUST] as Boolean,
						document.data[MIX] as Boolean,
						document.data[UPLOAD] as Boolean
				)
				array.add(variant)
			}
			return array
		}
	}

	@Ignore
	constructor(idVariant: Long, name: String, type: Int, isMust: Boolean, isMix: Boolean, isUploaded: Boolean): this(name, type, isMust, isMix, isUploaded){
		this.id = idVariant
	}

	@Ignore
	constructor(idVariant: Long, name: String, type: Int, isMust: Boolean, isMix: Boolean): this(name, type, isMust, isMix, false){
		this.id = idVariant
	}

	@Ignore
	constructor(name: String, type: Int, isMust: Boolean, isMix: Boolean): this(name, type, isMust, isMix, false)
}
