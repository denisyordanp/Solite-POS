package com.socialite.solite_pos.schema

import com.socialite.solite_pos.view.ui.DropdownItem
import java.io.Serializable

data class Promo(
    val id: String,
    override val name: String,
    val desc: String,
    val isCash: Boolean,
    val value: Int?,
    val isActive: Boolean,
    val isUploaded: Boolean
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
}
