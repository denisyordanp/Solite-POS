package com.sosialite.solite_pos.data.source.local.entity.room.master

import androidx.room.*
import com.sosialite.solite_pos.data.source.local.room.AppDatabase
import java.io.Serializable

@Entity(
		tableName = AppDatabase.TBL_VARIANT,
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
		var isMix: Boolean
) : Serializable{

	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = ID)
	var id: Int = 0

	companion object{
		const val ID = "id_variant"
		const val NAME = "name"
		const val TYPE = "type"
		const val MUST = "must"
		const val MIX = "mix"

		const val ONE_OPTION = 1
		const val MULTIPLE_OPTION = 2
	}

	constructor(idVariant: Int, name: String, type: Int, isMust: Boolean, isMix: Boolean): this(name, type, isMust, isMix){
		this.id = idVariant
	}
}
