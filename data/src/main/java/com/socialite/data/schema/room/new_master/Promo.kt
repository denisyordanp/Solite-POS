package com.socialite.data.schema.room.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.sqlite.db.SimpleSQLiteQuery
import com.socialite.data.database.AppDatabase.Companion.UPLOAD
import com.socialite.data.schema.response.PromoResponse
import com.socialite.data.schema.room.EntityData

@Entity(
    tableName = Promo.DB_NAME,
    indices = [
        Index(value = [Promo.ID])
    ]
)
data class Promo(
    @PrimaryKey
    @ColumnInfo(name = ID, defaultValue = "")
    override val id: String,

    @ColumnInfo(name = NAME)
    val name: String,

    @ColumnInfo(name = DESC)
    val desc: String,

    @ColumnInfo(name = CASH)
    val isCash: Boolean,

    @ColumnInfo(name = VALUE)
    val value: Int?,

    @ColumnInfo(name = STATUS)
    val isActive: Boolean,

    @ColumnInfo(name = UPLOAD)
    val isUploaded: Boolean
) : EntityData {

    fun toResponse(): PromoResponse {
        return PromoResponse(
            id = id,
            name = name,
            desc = desc,
            value = value,
            isCash = isCash,
            isActive = isActive,
            isUploaded = true
        )
    }

    companion object {
        const val ID = "id_promo"
        const val STATUS = "status"
        const val NAME = "name"
        const val DESC = "desc"
        const val CASH = "cash"
        const val VALUE = "value"

        const val DB_NAME = "new_promo"
        const val ID_ADD = "add_id"

        const val ALL = 2
        const val ACTIVE = 1

        fun filter(status: Int): SimpleSQLiteQuery {
            val query = StringBuilder().append("SELECT * FROM ")
            query.append(DB_NAME)
            when (status) {
                ACTIVE -> {
                    query.append(" WHERE ")
                        .append(STATUS)
                        .append(" = ").append(status)
                }

                else -> {
                    // Do nothing
                }
            }
            return SimpleSQLiteQuery(query.toString())
        }
    }
}
