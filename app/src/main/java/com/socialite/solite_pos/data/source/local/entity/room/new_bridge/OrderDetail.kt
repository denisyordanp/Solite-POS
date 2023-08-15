package com.socialite.solite_pos.data.source.local.entity.room.new_bridge

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.solite_pos.data.source.local.entity.helper.EntityData
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Order
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Product
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.data.schema.response.OrderDetailResponse
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
        override var id: String,

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
