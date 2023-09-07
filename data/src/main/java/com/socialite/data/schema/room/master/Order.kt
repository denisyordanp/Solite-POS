package com.socialite.data.schema.room.master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.socialite.data.database.AppDatabase
import com.socialite.data.database.AppDatabase.Companion.UPLOAD

@Entity(
        tableName = Order.DB_NAME,
        primaryKeys = [Order.NO],
        foreignKeys = [
            ForeignKey(
                    entity = Customer::class,
                    parentColumns = [Customer.ID],
                    childColumns = [Customer.ID],
                    onDelete = ForeignKey.CASCADE
            )
        ],
        indices = [
            Index(value = [Customer.ID])
        ]
)
data class Order(
        @ColumnInfo(name = NO)
        var orderNo: String,

        @ColumnInfo(name = AppDatabase.REPLACED_UUID, defaultValue = "")
        val new_id: String,

        @ColumnInfo(name = Customer.ID)
        var customer: Long,

        @ColumnInfo(name = ORDER_DATE)
        var orderTime: String,

        @ColumnInfo(name = COOK_TIME)
        var cookTime: String?,

        @ColumnInfo(name = TAKE_AWAY)
        var isTakeAway: Boolean,

        @ColumnInfo(name = STATUS)
        var status: Int,

        @ColumnInfo(name = STORE)
        var store: Long,

        @ColumnInfo(name = UPLOAD)
        var isUploaded: Boolean
) {
    companion object {
        const val ORDER_DATE = "order_date"
        const val COOK_TIME = "cook_time"
        const val TAKE_AWAY = "take_away"
        const val STATUS = "status"
        const val STORE = "store"
        const val NO = "order_no"
        const val DB_NAME = "order"
    }
}
