package com.socialite.schema.ui.main

import java.util.UUID

data class Category(
    val id: String,
    val name: String,
    val desc: String,
    val isActive: Boolean,
    val isUploaded: Boolean
) {
    fun isNewCategory() = id == ID_ADD

    fun asNewCategory() = this.copy(
        id = UUID.randomUUID().toString()
    )

    companion object {
        private const val ID_ADD = "add_id"

        fun createNewCategory(
            name: String,
            desc: String
        ) = Category(
            id = ID_ADD,
            name = name,
            desc = desc,
            isActive = true,
            isUploaded = false
        )
    }

    enum class Status(val code: Int) {
        ALL(2), ACTIVE(1)
    }
}
