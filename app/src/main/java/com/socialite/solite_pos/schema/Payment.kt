package com.socialite.solite_pos.schema

import com.socialite.solite_pos.view.ui.DropdownItem

import com.socialite.data.schema.room.new_master.Payment as DataPayment

data class Payment(
    val id: String,
    override val name: String,
    val desc: String,
    val tax: Float,
    val isCash: Boolean,
    val isActive: Boolean,
    val isUploaded: Boolean
) : DropdownItem {
    companion object {
        fun fromData(data: DataPayment): Payment {
            return Payment(
                data.id,
                data.name,
                data.desc,
                data.tax,
                data.isCash,
                data.isActive,
                data.isUploaded
            )
        }
    }
}
