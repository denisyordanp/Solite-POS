package com.socialite.data.schema.room.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.data.database.AppDatabase.Companion.UPLOAD

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
    var name: String,

    @ColumnInfo(name = DESC)
    var desc: String,

    @ColumnInfo(name = STATUS)
    var isActive: Boolean,

    @ColumnInfo(name = UPLOAD)
    var isUploaded: Boolean
) {

    companion object {
        const val ID = "id_category"
        const val STATUS = "status"
        const val NAME = "name"
        const val DESC = "desc"

        const val DB_NAME = "new_category"
    }
}
