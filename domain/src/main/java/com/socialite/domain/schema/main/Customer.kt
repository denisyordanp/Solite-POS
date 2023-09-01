package com.socialite.domain.schema.main

import java.util.UUID

data class Customer(
    val id: String,
    var name: String,
    var isUploaded: Boolean
) {
    fun isAdd() = id == ID_ADD

    companion object {
        const val ID_ADD = "add_id"

        fun add(name: String) = Customer(
            id = ID_ADD,
            name = name,
            isUploaded = false
        )

        fun createNew(name: String) = Customer(
            id = UUID.randomUUID().toString(),
            name = name,
            isUploaded = false
        )
    }
}
