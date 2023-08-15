package com.socialite.solite_pos.data.schema.room.master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.sqlite.db.SimpleSQLiteQuery
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.view.ui.DropdownItem
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

        @ColumnInfo(name = AppDatabase.REPLACED_UUID, defaultValue = "")
        val new_id: String,

        @ColumnInfo(name = NAME)
        override var name: String,

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
) : Serializable, DropdownItem {

    companion object {
        const val ID = "id_promo"
        const val STATUS = "status"
        const val NAME = "name"
        const val DESC = "desc"
        const val CASH = "cash"
        const val VALUE = "value"

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
