package com.socialite.solite_pos.schema

import com.socialite.solite_pos.view.ui.DropdownItem

data class Payment(
    val id: String,
    override val name: String,
    val desc: String,
    val tax: Float,
    val isCash: Boolean,
    val isActive: Boolean,
    val isUploaded: Boolean
) : DropdownItem
