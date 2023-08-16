package com.socialite.data.schema.room.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.sqlite.db.SimpleSQLiteQuery
import com.socialite.data.database.AppDatabase.Companion.UPLOAD
import com.socialite.data.schema.response.CategoryResponse
import com.socialite.data.schema.room.EntityData
import java.io.Serializable
import java.util.UUID

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
    var name: String,

    @ColumnInfo(name = DESC)
    var desc: String,

    @ColumnInfo(name = STATUS)
    var isActive: Boolean,

    @ColumnInfo(name = UPLOAD)
    var isUploaded: Boolean
) : Serializable, EntityData {

    fun isNewCategory() = id == ID_ADD

    fun asNewCategory() = this.copy(
        id = UUID.randomUUID().toString()
    )

    fun toResponse(): CategoryResponse {
        return CategoryResponse(
            id = id,
            name = name,
            desc = desc,
            isActive = isActive,
            isUploaded = true
        )
    }

    companion object {
        const val ID = "id_category"
        const val STATUS = "status"
        const val NAME = "name"
        const val DESC = "desc"

        const val DB_NAME = "new_category"
        private const val ID_ADD = "add_id"

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

        fun createNewCategory(
            name: String,
            desc: String
        ) = Category(
            id = ID_ADD,
            name = name,
            desc = desc,
            isActive = true,
            isUploaded = false
        )
    }
}
