package com.socialite.data.schema.response

import com.socialite.schema.database.new_master.Outcome

data class OutcomeResponse(
    val amount: Int,
    val date: String,
    val desc: String,
    val id: String,
    val isUploaded: Boolean,
    val name: String,
    val price: Int,
    val store: String,
    val user: Long
) {

    fun toEntity(): Outcome {
        return Outcome(
            id = id,
            name = name,
            desc = desc,
            price = price.toLong(),
            amount = amount,
            date = date,
            store = store,
            isUploaded = isUploaded,
            user = user
        )
    }
}
