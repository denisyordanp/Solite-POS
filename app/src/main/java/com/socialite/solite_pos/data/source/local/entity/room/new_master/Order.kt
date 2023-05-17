package com.socialite.solite_pos.data.source.local.entity.room.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.utils.config.DateUtils.Companion.DATE_WITH_TIME_FORMAT
import com.socialite.solite_pos.utils.config.DateUtils.Companion.convertDateFromDb
import com.socialite.solite_pos.view.ui.OrderMenus
import java.io.Serializable
import java.util.UUID

@Entity(
    tableName = Order.DB_NAME,
    primaryKeys = [Order.ID],
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
) : Serializable {

    companion object {

        const val ORDER_DATE = "order_date"
        const val TAKE_AWAY = "take_away"
        const val STATUS = "status"
        const val NO = "order_no"
        const val ID = "id"

        const val DB_NAME = "new_order"

        const val ON_PROCESS = 0
        const val NEED_PAY = 1
        const val CANCEL = 2
        const val DONE = 3
    }

    @Ignore
    constructor(orderNo: String, customer: String, orderTime: String) : this(
        id = UUID.randomUUID().toString(),
        orderNo = orderNo,
        customer = customer,
        orderTime = orderTime,
        isTakeAway = false,
        status = ON_PROCESS,
        store = Store.ID_ADD,
        isUploaded = false
    )

    @Ignore
    constructor(
        orderNo: String,
        customer: String,
        orderTime: String,
        store: String,
        isTakeAway: Boolean
    ) : this(
        id = UUID.randomUUID().toString(),
        orderNo = orderNo,
        customer = customer,
        orderTime = orderTime,
        isTakeAway = isTakeAway,
        status = ON_PROCESS,
        store = store,
        isUploaded = false
    )

    fun isEditable() = status == ON_PROCESS || status == NEED_PAY

    fun getQueueNumber(): String {
        return orderNo.substring(orderNo.length - 3, orderNo.length)
    }

    val timeString: String
        get() {
            return convertDateFromDb(orderTime, DATE_WITH_TIME_FORMAT)
        }

    fun statusToOrderMenu() = OrderMenus.values().find { it.status == status }
}
