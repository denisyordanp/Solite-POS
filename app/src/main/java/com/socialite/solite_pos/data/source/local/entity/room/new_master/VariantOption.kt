package com.socialite.solite_pos.data.source.local.entity.room.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.sqlite.db.SimpleSQLiteQuery
import com.socialite.solite_pos.data.source.local.entity.helper.EntityData
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.data.source.remote.response.entity.VariantOptionResponse
import java.io.Serializable
import java.util.UUID

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
    var variant: String,

    @ColumnInfo(name = NAME)
    var name: String,

    @ColumnInfo(name = DESC)
    var desc: String,

    @ColumnInfo(name = STATUS)
    var isActive: Boolean,

    @ColumnInfo(name = UPLOAD)
    var isUploaded: Boolean
) : Serializable, EntityData {

    fun isAdd() = id == ID_ADD

    fun asNewVariantOption() = this.copy(
            id = UUID.randomUUID().toString()
    )

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

        fun getFilter(idVariant: String, state: Int): SimpleSQLiteQuery {
            val query = StringBuilder().append("SELECT * FROM ")
            query.append(DB_NAME)
            query.append(" WHERE ")
                .append(Variant.ID)
                .append(" = ")
                .append("'${idVariant}'")
            if (state == ACTIVE) {
                query.append(" AND ")
                    .append(STATUS)
                    .append(" = ").append(ACTIVE)
            }
            return SimpleSQLiteQuery(query.toString())
        }

        fun createNew(
                variant: String,
                name: String,
                desc: String,
                isActive: Boolean
        ) = VariantOption(
                id = ID_ADD,
                variant = variant,
                name = name,
                desc = desc,
                isActive = isActive,
                isUploaded = false
        )
    }
}
