package com.socialite.schema.database.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.schema.database.EntityData

@Entity(
    tableName = Variant.DB_NAME,
    indices = [
        Index(value = [Variant.ID])
    ]
)
data class Variant(
    @PrimaryKey
    @ColumnInfo(name = ID, defaultValue = "")
    override val id: String,

    @ColumnInfo(name = NAME)
    val name: String,

    @ColumnInfo(name = TYPE)
    val type: Int,

    @ColumnInfo(name = MUST)
    val isMust: Boolean? = null,

    @ColumnInfo(name = UPLOAD)
    val isUploaded: Boolean
) : EntityData {

    companion object {
        const val ID = "id_variant"
        const val NAME = "name"
        const val TYPE = "type"
        const val MUST = "must"
        const val UPLOAD = "upload"

        const val DB_NAME = "new_variant"
        const val ID_ADD = "add_id"
    }
}
