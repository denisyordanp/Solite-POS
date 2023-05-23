package com.socialite.solite_pos.data.source.local.entity.room.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.data.source.remote.response.entity.VariantResponse
import java.io.Serializable
import java.util.UUID

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
) : Serializable {

    fun isSingleOption() = type == ONE_OPTION

    fun isAdd() = id == ID_ADD

    fun asNewVariant() = this.copy(
            id = UUID.randomUUID().toString()
    )

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

        const val ONE_OPTION = 1
        const val MULTIPLE_OPTION = 2

        fun createNew(
                name: String, type: Int, isMust: Boolean
        ) = Variant(
                id = ID_ADD,
                name = name,
                type = type,
                isMust = isMust,
                isUploaded = false
        )
    }
}
