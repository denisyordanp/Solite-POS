package com.socialite.schema.database.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.sqlite.db.SimpleSQLiteQuery
import com.socialite.schema.database.EntityData

@Entity(
    tableName = Category.DB_NAME,
    indices = [
        Index(value = [Category.ID])
    ]
)
data class Category(

    @PrimaryKey
    @ColumnInfo(name = ID, defaultValue = "")
    override val id: String,

    @ColumnInfo(name = NAME)
    val name: String,

    @ColumnInfo(name = DESC)
    val desc: String,

    @ColumnInfo(name = STATUS)
    val isActive: Boolean,

    @ColumnInfo(name = UPLOAD)
    val isUploaded: Boolean
) : EntityData {

    companion object {
        const val ID = "id_category"
        const val STATUS = "status"
        const val NAME = "name"
        const val DESC = "desc"
        const val UPLOAD = "upload"

        const val DB_NAME = "new_category"

        const val ALL = 2
        const val ACTIVE = 1

        fun getFilter(state: Int): SimpleSQLiteQuery {
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
