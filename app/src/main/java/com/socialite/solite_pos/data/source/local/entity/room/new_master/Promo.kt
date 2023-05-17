package com.socialite.solite_pos.data.source.local.entity.room.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.sqlite.db.SimpleSQLiteQuery
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.view.ui.DropdownItem
import java.io.Serializable

@Entity(
    tableName = Promo.DB_NAME,
    indices = [
        Index(value = [Promo.ID])
    ]
)
data class Promo(
    @PrimaryKey
    @ColumnInfo(name = ID, defaultValue = "")
    val id: String,

    @ColumnInfo(name = NAME)
    override var name: String,

    @ColumnInfo(name = DESC)
    var desc: String,

    @ColumnInfo(name = CASH)
    var isCash: Boolean,

    @ColumnInfo(name = VALUE)
    var value: Int?,

    @ColumnInfo(name = STATUS)
    var isActive: Boolean,

    @ColumnInfo(name = UPLOAD)
    var isUploaded: Boolean
) : Serializable, DropdownItem {

    fun isNewPromo() = id == ID_ADD

    fun isManualInput() = isCash && value == null

    fun calculatePromo(total: Long, manualInput: Long?): Long {
        return if (isCash) {
            if (isManualInput()) {
                manualInput ?: 0L
            } else {
                value?.toLong() ?: 0L
            }
        } else {
            ((value!!.toFloat() / 100) * total).toLong()
        }
    }

    companion object {
        const val ID = "id_promo"
        const val STATUS = "status"
        const val NAME = "name"
        const val DESC = "desc"
        const val CASH = "cash"
        const val VALUE = "value"

        const val DB_NAME = "new_promo"
        const val ID_ADD = "add_id"

        fun filter(status: Status): SimpleSQLiteQuery {
            val query = StringBuilder().append("SELECT * FROM ")
            query.append(DB_NAME)
            when (status) {
                Status.ACTIVE -> {
                    query.append(" WHERE ")
                        .append(STATUS)
                        .append(" = ").append(status.code)
                }

                else -> {
                    // Do nothing
                }
            }
            return SimpleSQLiteQuery(query.toString())
        }

        fun createNewPromo(
            name: String,
            desc: String,
            isCash: Boolean,
            value: Int?
        ) = Promo(
            id = ID_ADD,
            name = name,
            desc = desc,
            isCash = isCash,
            value = value,
            isActive = true,
            isUploaded = false
        )
    }

    enum class Status(val code: Int) {
        ALL(2),
        ACTIVE(1)
    }
}
