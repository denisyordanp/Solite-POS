package com.socialite.solite_pos.data.schema.room.master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.solite_pos.database.AppDatabase.Companion.UPLOAD
import java.io.Serializable

@Entity(
    tableName = PurchaseProduct.DB_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Purchase::class,
            parentColumns = [Purchase.NO],
            childColumns = [Purchase.NO],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Product::class,
            parentColumns = [Product.ID],
            childColumns = [Product.ID],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = [PurchaseProduct.ID]),
        Index(value = [Purchase.NO]),
        Index(value = [Product.ID])
    ]
)
data class PurchaseProduct(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    var id: Long,

    @ColumnInfo(name = Purchase.NO)
    var purchaseNo: String,

    @ColumnInfo(name = Product.ID)
    var idProduct: Long,

    @ColumnInfo(name = AMOUNT)
    var amount: Int,

    @ColumnInfo(name = UPLOAD)
    var isUploaded: Boolean
) : Serializable {

    companion object {
        const val ID = "id_purchase_product"
        const val AMOUNT = "amount"

        const val DB_NAME = "purchase_product"
    }
}
