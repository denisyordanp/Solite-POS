package com.socialite.schema.database.master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = Supplier.DB_NAME,
    indices = [
        Index(value = [Supplier.ID]),
    ]
)
data class Supplier(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    var id: Long,

    @ColumnInfo(name = NAME)
    var name: String,

    @ColumnInfo(name = PHONE)
    var phone: String,

    @ColumnInfo(name = ADDRESS)
    var address: String,

    @ColumnInfo(name = DESC)
    var desc: String,

    @ColumnInfo(name = UPLOAD)
    var isUploaded: Boolean
) : Serializable {
    companion object {
        const val ID = "id_supplier"
        const val ADDRESS = "address"
        const val PHONE = "phone"
        const val NAME = "name"
        const val DESC = "desc"
        const val UPLOAD = "upload"
        const val REPLACED_UUID = "replaced_uuid"

        const val DB_NAME = "supplier"
    }
}
