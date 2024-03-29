package com.socialite.data.schema.room.new_bridge

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.data.database.AppDatabase.Companion.UPLOAD
import com.socialite.data.schema.response.OrderDetailResponse
import com.socialite.data.schema.room.EntityData
import com.socialite.data.schema.room.new_master.Order
import com.socialite.data.schema.room.new_master.Product
import java.io.Serializable
import java.util.UUID

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
    override val id: String,

    @ColumnInfo(name = Order.ID)
    val order: String,

    @ColumnInfo(name = Product.ID)
    val product: String,

    @ColumnInfo(name = AMOUNT)
    val amount: Int,

    @ColumnInfo(name = UPLOAD)
    val isUpload: Boolean,

    @ColumnInfo(name = DELETED, defaultValue = "0")
    val isDeleted: Boolean
) : Serializable, EntityData {

    fun toResponse(): OrderDetailResponse {
        return OrderDetailResponse(
            id = id,
            amount = amount,
            order = order,
            product = product,
            isUploaded = true
        )
    }

    companion object {
        const val ID = "id_order_detail"
        const val AMOUNT = "amount"
        const val DELETED = "deleted"

        const val DB_NAME = "new_order_detail"

        fun createNewOrderDetail(
            orderId: String,
            idProduct: String,
            amount: Int
        ) = OrderDetail(
            id = UUID.randomUUID().toString(),
            order = orderId,
            product = idProduct,
            amount = amount,
            isUpload = false,
            isDeleted = false
        )
    }
}
