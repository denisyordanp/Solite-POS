package com.socialite.solite_pos.data.source.local.entity.room.master

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.data.source.preference.SettingPref
import com.socialite.solite_pos.utils.config.DateUtils.Companion.DATE_WITH_TIME_FORMAT
import com.socialite.solite_pos.utils.config.DateUtils.Companion.convertDateFromDb
import com.socialite.solite_pos.utils.config.DateUtils.Companion.strToDate
import com.socialite.solite_pos.view.ui.OrderMenus
import java.io.Serializable
import java.util.Calendar

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
) : Serializable {

    companion object {

        const val ORDER_DATE = "order_date"
        const val COOK_TIME = "cook_time"
        const val TAKE_AWAY = "take_away"
        const val STATUS = "status"
        const val STORE = "store"
        const val NO = "order_no"

        const val DB_NAME = "order"

        const val ON_PROCESS = 0
        const val NEED_PAY = 1
        const val CANCEL = 2
        const val DONE = 3
    }

    @Ignore
    constructor(orderNo: String, customer: Long, orderTime: String) : this(
        orderNo,
        customer,
        orderTime,
        null,
        false,
        ON_PROCESS,
        0L,
        false
    )

    @Ignore
    constructor(
        orderNo: String,
        customer: Long,
        orderTime: String,
        store: Long,
        isTakeAway: Boolean
    ) : this(
        orderNo,
        customer,
        orderTime,
        null,
        isTakeAway,
        ON_PROCESS,
        store,
        false
    )

    fun isEditable() = status == ON_PROCESS

    fun getQueueNumber(): String {
        return orderNo.substring(orderNo.length - 3, orderNo.length)
    }

    val timeString: String
        get() {
            return convertDateFromDb(orderTime, DATE_WITH_TIME_FORMAT)
        }

    fun getFinishCook(context: Context): Calendar {
        return if (cookTime != null) {
            val finish: Calendar = Calendar.getInstance()

            finish.time = strToDate(cookTime)
            finish.add(Calendar.MINUTE, SettingPref(context).cookTime)
            finish
        } else {
            Calendar.getInstance()
        }
    }

    fun statusToOrderMenu() = OrderMenus.values().find { it.status == status }
}
