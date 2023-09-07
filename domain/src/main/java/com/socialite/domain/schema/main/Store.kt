package com.socialite.domain.schema.main

import java.util.UUID

data class Store(
    val id: String,
    val name: String,
    val address: String,
    val isUploaded: Boolean
) {

    fun isNewStore() = id == ID_ADD

    fun asNewStore() = this.copy(
        id = UUID.randomUUID().toString()
    )

    companion object {
        const val ID_ADD = "add_id"

        fun add(name: String, address: String): Store {
            return Store(
                id = ID_ADD,
                name = name,
                address = address,
                isUploaded = false
            )
        }
    }
}

