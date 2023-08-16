package com.socialite.data.schema.room.new_bridge

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.data.database.AppDatabase.Companion.UPLOAD
import com.socialite.data.schema.room.new_master.Order
import com.socialite.data.schema.room.new_master.Product
import java.io.Serializable

@Entity(
    tableName = OrderDetail.DB_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Order::class,
            parentColumns = [Order.ID],
            childColumns = [Order.ID],
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
        Index(value = [OrderDetail.ID]),
        Index(value = [Order.ID]),
        Index(value = [Product.ID])
    ]
)
data class OrderDetail(
    @PrimaryKey
    @ColumnInfo(name = ID, defaultValue = "")
    var id: String,

    @ColumnInfo(name = Order.ID)
    var order: String,

    @ColumnInfo(name = Product.ID)
    var product: String,

    @ColumnInfo(name = AMOUNT)
    var amount: Int,

    @ColumnInfo(name = UPLOAD)
    var isUpload: Boolean,

    @ColumnInfo(name = DELETED, defaultValue = "0")
    var isDeleted: Boolean
) : Serializable {
    companion object {
        const val ID = "id_order_detail"
        const val AMOUNT = "amount"
        const val DELETED = "deleted"
        const val DB_NAME = "new_order_detail"
    }
}
