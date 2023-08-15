package com.socialite.solite_pos.data.schema.room.master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.sqlite.db.SimpleSQLiteQuery
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import java.io.Serializable

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

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = ID)
        var id: Long,

        @ColumnInfo(name = AppDatabase.REPLACED_UUID, defaultValue = "")
        val new_id: String,

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
) : Serializable {

    fun isAdd() = id == 0L

    companion object {
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
            if (state == ACTIVE) {
                query.append(" AND ")
                        .append(STATUS)
                        .append(" = ").append(ACTIVE)
            }
            return SimpleSQLiteQuery(query.toString())
        }
    }

}
