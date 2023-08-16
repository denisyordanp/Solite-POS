package com.socialite.data.schema.room.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.data.database.AppDatabase.Companion.UPLOAD

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
    val id: String,

    @ColumnInfo(name = Variant.ID)
    var variant: String,

    @ColumnInfo(name = NAME)
    var name: String,

    @ColumnInfo(name = DESC)
    var desc: String,

    @ColumnInfo(name = STATUS)
    var isActive: Boolean,

    @ColumnInfo(name = UPLOAD)
    var isUploaded: Boolean
) {

    companion object {
        const val ID = "id_variant_option"
        const val STATUS = "status"
        const val DESC = "desc"
        const val NAME = "name"

        const val DB_NAME = "new_variant_option"
    }
}
