package com.socialite.data.schema.room.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.data.database.AppDatabase.Companion.UPLOAD
import com.socialite.data.schema.response.VariantOptionResponse
import com.socialite.data.schema.room.EntityData

@Entity(
    tableName = VariantOption.DB_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Variant::class,
            parentColumns = [Variant.ID],
            childColumns = [Variant.ID],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = [VariantOption.ID]),
        Index(value = [Variant.ID])
    ]
)
data class VariantOption(
    @PrimaryKey
    @ColumnInfo(name = ID, defaultValue = "")
    override val id: String,

    @ColumnInfo(name = Variant.ID)
    val variant: String,

    @ColumnInfo(name = NAME)
    val name: String,

    @ColumnInfo(name = DESC)
    val desc: String,

    @ColumnInfo(name = STATUS)
    val isActive: Boolean,

    @ColumnInfo(name = UPLOAD)
    val isUploaded: Boolean
) : EntityData {

    fun toResponse(): VariantOptionResponse {
        return VariantOptionResponse(
            id = id,
            name = name,
            desc = desc,
            variant = variant,
            isActive = isActive,
            isUploaded = true
        )
    }

    companion object {
        const val ID = "id_variant_option"
        const val STATUS = "status"
        const val DESC = "desc"
        const val NAME = "name"

        const val ALL = 2
        const val ACTIVE = 1

        const val DB_NAME = "new_variant_option"
        const val ID_ADD = "add_id"
    }
}
