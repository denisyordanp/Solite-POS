package com.socialite.solite_pos.data.source.local.entity.room.master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.firebase.firestore.QuerySnapshot
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.data.source.remote.response.helper.RemoteClassUtils
import com.socialite.solite_pos.view.ui.DropdownItem
import java.io.Serializable

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
    companion object : RemoteClassUtils<Payment> {
        const val ID = "id_payment"
        const val STATUS = "status"
        const val NAME = "name"
        const val DESC = "desc"
        const val CASH = "cash"
        const val TAX = "tax"

        const val DB_NAME = "payment"
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
    ) : this(id, name, desc, tax, isCash, isActive, false)

    @Ignore
    constructor(
        name: String,
        desc: String,
        tax: Float,
        isCash: Boolean,
        isActive: Boolean
    ) : this(0, name, desc, tax, isCash, isActive, false)
}
