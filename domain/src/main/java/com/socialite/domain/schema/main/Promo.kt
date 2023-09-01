package com.socialite.domain.schema.main

import java.util.UUID

data class Promo(
    val id: String,
    val name: String,
    val desc: String,
    val isCash: Boolean,
    val value: Int?,
    val isActive: Boolean,
    val isUploaded: Boolean
) {

    fun isNewPromo() = id == ID_ADD

    fun asNewPromo() = this.copy(
        id = UUID.randomUUID().toString()
    )

    companion object {
        const val ID_ADD = "add_id"

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
