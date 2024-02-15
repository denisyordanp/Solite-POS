package com.socialite.schema.ui.helper

import java.util.UUID

data class Outcome(
    val id: String,
    val name: String,
    val desc: String,
    val price: Long,
    val amount: Int,
    val date: String,
    val store: String,
    val user: Long,
    val isUploaded: Boolean
) {

    val isNewOutcome get() = store == "" && user == 0L

    val total: Long
        get() = price * amount

    companion object {

        fun createNewOutcome(name: String, desc: String, price: Long, date: String) =
            Outcome(
                id = UUID.randomUUID().toString(),
                name = name,
                desc = desc,
                price = price,
                amount = 1,
                date = date,
                store = "",
                isUploaded = false,
                user = 0L
            )
    }
}
