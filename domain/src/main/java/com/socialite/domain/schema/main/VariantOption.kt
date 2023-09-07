package com.socialite.domain.schema.main

import java.util.UUID

data class VariantOption(
    val id: String,
    var variant: String,
    var name: String,
    var desc: String,
    var isActive: Boolean,
    var isUploaded: Boolean
) {

    fun isAdd() = id == ID_ADD

    fun asNewVariantOption() = this.copy(
        id = UUID.randomUUID().toString()
    )

    companion object {
        const val ID_ADD = "add_id"

        fun createNew(
            variant: String,
            name: String,
            desc: String,
            isActive: Boolean
        ) = VariantOption(
            id = ID_ADD,
            variant = variant,
            name = name,
            desc = desc,
            isActive = isActive,
            isUploaded = false
        )
    }
}
