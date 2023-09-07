package com.socialite.domain.schema.main

import java.util.UUID

data class Variant(
    val id: String,
    var name: String,
    var type: Int,
    var isMust: Boolean? = null,
    var isUploaded: Boolean
) {

    fun isSingleOption() = type == ONE_OPTION

    fun isAdd() = id == ID_ADD

    fun asNewVariant() = this.copy(
        id = UUID.randomUUID().toString()
    )

    companion object {
        const val ID_ADD = "add_id"

        const val ONE_OPTION = 1
        const val MULTIPLE_OPTION = 2

        fun createNew(
            name: String, type: Int, isMust: Boolean
        ) = Variant(
            id = ID_ADD,
            name = name,
            type = type,
            isMust = isMust,
            isUploaded = false
        )
    }
}
