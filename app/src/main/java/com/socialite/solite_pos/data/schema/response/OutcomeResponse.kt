package com.socialite.solite_pos.data.schema.response

import com.socialite.solite_pos.data.schema.room.new_master.Outcome

data class OutcomeResponse(
    val amount: Int,
    val date: String,
    val desc: String,
    val id: String,
    val isUploaded: Boolean,
    val name: String,
    val price: Int,
    val store: String
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
            isUploaded = isUploaded
        )
    }
}
