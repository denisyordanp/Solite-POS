package com.socialite.schema.database.master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.sqlite.db.SimpleSQLiteQuery
import java.io.Serializable

@Entity(
    tableName = Promo.DB_NAME,
    indices = [
        Index(value = [Promo.ID])
    ]
)
data class Promo(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    var id: Long,

    @ColumnInfo(name = REPLACED_UUID, defaultValue = "")
    val new_id: String,

    @ColumnInfo(name = NAME)
    var name: String,

    @ColumnInfo(name = DESC)
    var desc: String,

    @ColumnInfo(name = CASH)
    var isCash: Boolean,

    @ColumnInfo(name = VALUE)
    var value: Int?,

    @ColumnInfo(name = STATUS)
    var isActive: Boolean,

    @ColumnInfo(name = UPLOAD)
    var isUploaded: Boolean
) : Serializable {

    companion object {
        const val ID = "id_promo"
        const val STATUS = "status"
        const val NAME = "name"
        const val DESC = "desc"
        const val CASH = "cash"
        const val VALUE = "value"
        const val UPLOAD = "upload"
        const val REPLACED_UUID = "replaced_uuid"

        const val DB_NAME = "promo"

        fun filter(status: Status): SimpleSQLiteQuery {
            val query = StringBuilder().append("SELECT * FROM ")
            query.append(DB_NAME)
            when (status) {
                Status.ACTIVE -> {
                    query.append(" WHERE ")
                        .append(STATUS)
                        .append(" = ").append(status.code)
                }

                else -> {
                    // Do nothing
                }
            }
            return SimpleSQLiteQuery(query.toString())
        }
    }

    enum class Status(val code: Int) {
        ALL(2),
        ACTIVE(1)
    }
}
