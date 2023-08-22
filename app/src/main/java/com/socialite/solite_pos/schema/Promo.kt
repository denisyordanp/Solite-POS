package com.socialite.solite_pos.schema

import com.socialite.solite_pos.view.ui.DropdownItem
import java.io.Serializable
import com.socialite.data.schema.room.new_master.Promo as DataPromo

data class Promo(
    val id: String,
    override var name: String,
    var desc: String,
    var isCash: Boolean,
    var value: Int?,
    var isActive: Boolean,
    var isUploaded: Boolean
) : Serializable, DropdownItem {

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
        fun fromData(data: DataPromo): Promo {
            return Promo(
                data.id,
                data.name,
                data.desc,
                data.isCash,
                data.value,
                data.isActive,
                data.isUploaded
            )
        }
    }
}
