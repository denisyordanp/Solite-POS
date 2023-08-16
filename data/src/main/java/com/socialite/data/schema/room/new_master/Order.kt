package com.socialite.data.schema.room.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.data.database.AppDatabase.Companion.UPLOAD

@Entity(
    tableName = Order.DB_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Customer::class,
            parentColumns = [Customer.ID],
            childColumns = [Customer.ID],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = [Customer.ID]),
        Index(value = [Order.ID])
    ]
)
data class Order(
    @PrimaryKey
    @ColumnInfo(name = ID, defaultValue = "")
    val id: String,

    @ColumnInfo(name = NO)
    var orderNo: String,

    @ColumnInfo(name = Customer.ID)
    var customer: String,

    @ColumnInfo(name = ORDER_DATE)
    var orderTime: String,

    @ColumnInfo(name = TAKE_AWAY)
    var isTakeAway: Boolean,

    @ColumnInfo(name = STATUS)
    var status: Int,

    @ColumnInfo(name = Store.ID)
    var store: String,

    @ColumnInfo(name = UPLOAD)
    var isUploaded: Boolean
) {

    companion object {

        const val ORDER_DATE = "order_date"
        const val TAKE_AWAY = "take_away"
        const val STATUS = "status"
        const val NO = "order_no"
        const val ID = "order_id"

        const val DB_NAME = "new_order"

        const val ON_PROCESS = 0
        const val NEED_PAY = 1
        const val CANCEL = 2
        const val DONE = 3
    }
}
