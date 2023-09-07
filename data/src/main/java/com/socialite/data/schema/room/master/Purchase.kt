package com.socialite.data.schema.room.master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.socialite.data.database.AppDatabase.Companion.UPLOAD
import java.io.Serializable

@Entity(
    tableName = Purchase.DB_NAME,
    primaryKeys = [Purchase.NO],
    indices = [
        Index(value = [Purchase.NO])
    ]
)
data class Purchase(
    @ColumnInfo(name = NO)
    var purchaseNo: String,

    @ColumnInfo(name = Supplier.ID)
    var idSupplier: Long,

    @ColumnInfo(name = PURCHASE_DATE)
    var purchaseTime: String,

    @ColumnInfo(name = UPLOAD)
    var isUploaded: Boolean
) : Serializable {

    companion object {

        const val PURCHASE_DATE = "purchase_date"
        const val NO = "purchase_no"
        const val DB_NAME = "purchase"
    }

}
