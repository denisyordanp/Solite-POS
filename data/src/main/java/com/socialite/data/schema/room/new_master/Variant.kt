package com.socialite.data.schema.room.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.data.database.AppDatabase.Companion.UPLOAD

@Entity(
    tableName = Variant.DB_NAME,
    indices = [
        Index(value = [Variant.ID])
    ]
)
data class Variant(
    @PrimaryKey
    @ColumnInfo(name = ID, defaultValue = "")
    val id: String,

    @ColumnInfo(name = NAME)
    var name: String,

    @ColumnInfo(name = TYPE)
    var type: Int,

    @ColumnInfo(name = MUST)
    var isMust: Boolean? = null,

    @ColumnInfo(name = UPLOAD)
    var isUploaded: Boolean
) {

    companion object {
        const val ID = "id_variant"
        const val NAME = "name"
        const val TYPE = "type"
        const val MUST = "must"

        const val DB_NAME = "new_variant"
    }
}
