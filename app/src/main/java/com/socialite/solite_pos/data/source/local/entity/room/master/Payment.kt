package com.socialite.solite_pos.data.source.local.entity.room.master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.sqlite.db.SimpleSQLiteQuery
import com.google.firebase.firestore.QuerySnapshot
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.data.source.remote.response.helper.RemoteClassUtils
import com.socialite.solite_pos.view.ui.DropdownItem
import java.io.Serializable
import java.util.UUID

@Entity(
    tableName = Payment.DB_NAME,
    indices = [
        Index(value = [Payment.ID])
    ]
)
data class Payment(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    var id: Long,

    @ColumnInfo(name = AppDatabase.REPLACED_UUID, defaultValue = "")
    val new_id: String,

    @ColumnInfo(name = NAME)
    override var name: String,

    @ColumnInfo(name = DESC)
    var desc: String,

    @ColumnInfo(name = TAX)
    var tax: Float,

    @ColumnInfo(name = CASH)
    var isCash: Boolean,

    @ColumnInfo(name = STATUS)
    var isActive: Boolean,

    @ColumnInfo(name = UPLOAD)
    var isUploaded: Boolean
) : Serializable, DropdownItem {

    fun isNewPayment() = id == 0L

    companion object : RemoteClassUtils<Payment> {
        const val ID = "id_payment"
        const val STATUS = "status"
        const val NAME = "name"
        const val DESC = "desc"
        const val CASH = "cash"
        const val TAX = "tax"

        const val DB_NAME = "payment"

        const val ALL = 2
        const val ACTIVE = 1

        fun createNewPayment(
            name: String,
            desc: String,
            isCash: Boolean
        ) = Payment(
            name = name,
            desc = desc,
            tax = 0f,
            isCash = isCash,
            isActive = true
        )

        fun filter(state: Int): SimpleSQLiteQuery {
            val query = StringBuilder().append("SELECT * FROM ")
            query.append(DB_NAME)
            when (state) {
                ACTIVE -> {
                    query.append(" WHERE ")
                        .append(STATUS)
                        .append(" = ").append(ACTIVE)
                }
            }
            return SimpleSQLiteQuery(query.toString())
        }

        override fun toHashMap(data: Payment): HashMap<String, Any?> {
            return hashMapOf(
                ID to data.id,
                NAME to data.name,
                DESC to data.desc,
                TAX to data.tax.toString(),
                CASH to data.isCash,
                STATUS to data.isActive,
                UPLOAD to data.isUploaded
            )
        }

        override fun toListClass(result: QuerySnapshot): List<Payment> {
            val array: ArrayList<Payment> = ArrayList()
            for (document in result) {
                val payment = Payment(
                    document.data[ID] as Long,
                    document.data[AppDatabase.REPLACED_UUID] as String,
                    document.data[NAME] as String,
                    document.data[DESC] as String,
                    (document.data[TAX] as String).toFloat(),
                    document.data[CASH] as Boolean,
                    document.data[STATUS] as Boolean,
                    document.data[UPLOAD] as Boolean
                )
                array.add(payment)
            }
            return array
        }
    }

    @Ignore
    constructor(
        id: Long,
        name: String,
        desc: String,
        tax: Float,
        isCash: Boolean,
        isActive: Boolean
    ) : this(id, UUID.randomUUID().toString(), name, desc, tax, isCash, isActive, false)

    @Ignore
    constructor(
        name: String,
        desc: String,
        tax: Float,
        isCash: Boolean,
        isActive: Boolean
    ) : this(0, UUID.randomUUID().toString(), name, desc, tax, isCash, isActive, false)
}
