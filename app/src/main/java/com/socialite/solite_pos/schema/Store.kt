package com.socialite.solite_pos.schema

import com.socialite.solite_pos.view.ui.DropdownItem

data class Store(
    val id: String,
    override val name: String,
    val address: String,
    val isUploaded: Boolean
) : DropdownItem
