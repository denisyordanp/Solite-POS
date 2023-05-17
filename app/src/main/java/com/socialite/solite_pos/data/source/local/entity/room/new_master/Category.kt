package com.socialite.solite_pos.data.source.local.entity.room.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.sqlite.db.SimpleSQLiteQuery
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.view.ui.DropdownItem
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
    val id: String,

    @ColumnInfo(name = NAME)
    override var name: String,

    @ColumnInfo(name = DESC)
    var desc: String,

    @ColumnInfo(name = STATUS)
    var isActive: Boolean,

    @ColumnInfo(name = UPLOAD)
    var isUploaded: Boolean
) : Serializable, DropdownItem {

    fun isNewCategory() = id.isEmpty()

    companion object {
        const val ID = "id_category"
        const val STATUS = "status"
        const val NAME = "name"
        const val DESC = "desc"

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

        fun createNewCategory(
            name: String,
            desc: String
        ) = Category(
            name = name,
            desc = desc,
            isActive = true
        )
    }

    @Ignore
    constructor(
        id: String,
        name: String,
        desc: String,
        isActive: Boolean
    ) : this(
        id = id,
        name = name,
        desc = desc,
        isActive = isActive,
        isUploaded = false
    )

    @Ignore
    constructor(name: String, desc: String, isActive: Boolean) : this(
        id = UUID.randomUUID().toString(),
        name = name,
        desc = desc,
        isActive = isActive,
        isUploaded = false
    )
}
