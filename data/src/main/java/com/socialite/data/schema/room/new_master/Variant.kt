package com.socialite.data.schema.room.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.data.database.AppDatabase.Companion.UPLOAD
import com.socialite.data.schema.response.VariantResponse
import com.socialite.data.schema.room.EntityData

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
    var name: String,

    @ColumnInfo(name = TYPE)
    var type: Int,

    @ColumnInfo(name = MUST)
    var isMust: Boolean? = null,

    @ColumnInfo(name = UPLOAD)
    var isUploaded: Boolean
) : EntityData {

    fun toResponse(): VariantResponse {
        return VariantResponse(
            id = id,
            name = name,
            type = type,
            isMust = isMust ?: false,
            isUploaded = true
        )
    }

    companion object {
        const val ID = "id_variant"
        const val NAME = "name"
        const val TYPE = "type"
        const val MUST = "must"

        const val DB_NAME = "new_variant"
        const val ID_ADD = "add_id"
    }
}
