package com.socialite.solite_pos.data.source.remote.response.entity

import com.socialite.solite_pos.data.source.local.entity.room.master.Outcome

data class OutcomeResponse(
    val amount: Int,
    val date: String,
    val desc: String,
    val id: String,
    val isUploaded: Boolean,
    val name: String,
    val price: Int,
    val store: Int
) {

    fun toEntity(): Outcome {
        return Outcome(
            id = id.toLong(),
            name = name,
            desc = desc,
            price = price.toLong(),
            amount = amount,
            date = date,
            store = store.toLong(),
            isUploaded = isUploaded
        )
    }
}
