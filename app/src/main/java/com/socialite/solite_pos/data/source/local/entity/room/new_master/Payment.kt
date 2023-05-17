package com.socialite.solite_pos.data.source.local.entity.room.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.sqlite.db.SimpleSQLiteQuery
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.view.ui.DropdownItem
import java.io.Serializable

@Entity(
    tableName = Payment.DB_NAME,
    indices = [
        Index(value = [Payment.ID])
    ]
)
data class Payment(

    @PrimaryKey
    @ColumnInfo(name = ID, defaultValue = "")
    val id: String,

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

    fun isNewPayment() = id == ID_ADD

    companion object {
        const val ID = "id_payment"
        const val STATUS = "status"
        const val NAME = "name"
        const val DESC = "desc"
        const val CASH = "cash"
        const val TAX = "tax"

        const val DB_NAME = "new_payment"

        const val ID_ADD = "add_id"
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
    }

    @Ignore
    constructor(
        id: String,
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
    ) : this(ID_ADD, name, desc, tax, isCash, isActive, false)

    // TODO: Create a new payment function to add the UUID
}
