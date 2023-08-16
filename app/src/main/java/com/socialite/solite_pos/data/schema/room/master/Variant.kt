package com.socialite.solite_pos.data.schema.room.master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.solite_pos.database.AppDatabase
import com.socialite.solite_pos.database.AppDatabase.Companion.UPLOAD
import java.io.Serializable

@Entity(
        tableName = Variant.DB_NAME,
        indices = [
            Index(value = [Variant.ID])
        ]
)
data class Variant(
        @ColumnInfo(name = AppDatabase.REPLACED_UUID, defaultValue = "")
        val new_id: String,

        @ColumnInfo(name = NAME)
        var name: String,

        @ColumnInfo(name = TYPE)
        var type: Int,

        @ColumnInfo(name = MUST)
        var isMust: Boolean? = null,

        @ColumnInfo(name = MIX)
        var isMix: Boolean,

        @ColumnInfo(name = UPLOAD)
        var isUploaded: Boolean
) : Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    var id: Long = 0

    companion object {
        const val ID = "id_variant"
        const val NAME = "name"
        const val TYPE = "type"
        const val MUST = "must"
        const val MIX = "mix"

        const val DB_NAME = "variant"

        const val ONE_OPTION = 1
        const val MULTIPLE_OPTION = 2
    }
}
