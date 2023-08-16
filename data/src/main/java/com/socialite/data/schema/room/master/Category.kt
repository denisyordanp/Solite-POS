package com.socialite.data.schema.room.master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.data.database.AppDatabase
import com.socialite.data.database.AppDatabase.Companion.UPLOAD
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

        @ColumnInfo(name = AppDatabase.REPLACED_UUID, defaultValue = "")
        val new_id: String,

        @ColumnInfo(name = NAME)
        var name: String,

        @ColumnInfo(name = DESC)
        var desc: String,

        @ColumnInfo(name = STOCK)
        var isStock: Boolean,

        @ColumnInfo(name = STATUS)
        var isActive: Boolean,

        @ColumnInfo(name = UPLOAD)
        var isUploaded: Boolean
) : Serializable {

    companion object {
        const val ID = "id_category"
        const val STATUS = "status"
        const val STOCK = "stock"
        const val NAME = "name"
        const val DESC = "desc"

        const val DB_NAME = "category"
    }
}
