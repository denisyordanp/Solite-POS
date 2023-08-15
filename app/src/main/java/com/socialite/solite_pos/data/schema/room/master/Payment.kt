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
        tableName = Payment.DB_NAME,
        indices = [
            Index(value = [Payment.ID])
        ]
)
data class Payment(

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = ID)
        var id: Long,

        @ColumnInfo(name = AppDatabase.REPLACED_UUID, defaultValue = "")
        val new_id: String,

        @ColumnInfo(name = NAME)
        override var name: String,

        @ColumnInfo(name = DESC)
        var desc: String,

        @ColumnInfo(name = TAX)
        var tax: Float,

        @ColumnInfo(name = CASH)
        var isCash: Boolean,

        @ColumnInfo(name = STATUS)
        var isActive: Boolean,

        @ColumnInfo(name = UPLOAD)
        var isUploaded: Boolean
) : Serializable, DropdownItem {

    companion object {
        const val ID = "id_payment"
        const val STATUS = "status"
        const val NAME = "name"
        const val DESC = "desc"
        const val CASH = "cash"
        const val TAX = "tax"

        const val DB_NAME = "payment"

        const val ALL = 2
        const val ACTIVE = 1

        fun filter(state: Int): SimpleSQLiteQuery {
            val query = StringBuilder().append("SELECT * FROM ")
            query.append(DB_NAME)
            when (state) {
                ACTIVE -> {
                    query.append(" WHERE ")
                            .append(STATUS)
                            .append(" = ").append(ACTIVE)
                }
            }
            return SimpleSQLiteQuery(query.toString())
        }
    }
}
