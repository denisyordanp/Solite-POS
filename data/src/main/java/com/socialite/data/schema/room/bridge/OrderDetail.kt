package com.socialite.data.schema.room.bridge

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.data.database.AppDatabase
import com.socialite.data.database.AppDatabase.Companion.UPLOAD
import com.socialite.data.schema.room.master.Order
import com.socialite.data.schema.room.master.Product
import java.io.Serializable

@Entity(
        tableName = OrderDetail.DB_NAME,
        foreignKeys = [
            ForeignKey(
                    entity = Order::class,
                    parentColumns = [Order.NO],
                    childColumns = [Order.NO],
                    onDelete = ForeignKey.CASCADE),
            ForeignKey(
                    entity = Product::class,
                    parentColumns = [Product.ID],
                    childColumns = [Product.ID],
                    onDelete = ForeignKey.CASCADE)
        ],
        indices = [
            Index(value = [OrderDetail.ID]),
            Index(value = [Order.NO]),
            Index(value = [Product.ID])
        ]
)
data class OrderDetail(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = ID)
        var id: Long,

        @ColumnInfo(name = AppDatabase.REPLACED_UUID, defaultValue = "")
        val new_id: String,

        @ColumnInfo(name = Order.NO)
        var orderNo: String,

        @ColumnInfo(name = Product.ID)
        var idProduct: Long,

        @ColumnInfo(name = AMOUNT)
        var amount: Int,

        @ColumnInfo(name = UPLOAD)
        var isUpload: Boolean
) : Serializable {
    companion object {
        const val ID = "id_order_detail"
        const val AMOUNT = "amount"

        const val DB_NAME = "order_detail"
    }
}
