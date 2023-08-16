package com.socialite.solite_pos.data.schema.room.master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.sqlite.db.SimpleSQLiteQuery
import com.socialite.data.database.AppDatabase.Companion.REPLACED_UUID
import com.socialite.data.database.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.view.ui.DropdownItem
import java.io.Serializable

@Entity(
        tableName = Category.DB_NAME,
        indices = [
            Index(value = [Category.ID])
        ]
)
data class Category(

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = ID)
        var id: Long,

        @ColumnInfo(name = REPLACED_UUID, defaultValue = "")
        val new_id: String,

        @ColumnInfo(name = NAME)
        override var name: String,

        @ColumnInfo(name = DESC)
        var desc: String,

        @ColumnInfo(name = STOCK)
        var isStock: Boolean,

        @ColumnInfo(name = STATUS)
        var isActive: Boolean,

        @ColumnInfo(name = UPLOAD)
        var isUploaded: Boolean
) : Serializable, DropdownItem {

    companion object {
        const val ID = "id_category"
        const val STATUS = "status"
        const val STOCK = "stock"
        const val NAME = "name"
        const val DESC = "desc"

        const val DB_NAME = "category"

        const val ALL = 2
        const val ACTIVE = 1

        fun getFilter(state: Int): SimpleSQLiteQuery {
            return filter(state)
        }

        private fun filter(state: Int): SimpleSQLiteQuery {
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
