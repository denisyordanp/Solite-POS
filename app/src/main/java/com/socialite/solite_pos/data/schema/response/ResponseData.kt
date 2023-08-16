package com.socialite.solite_pos.data.schema.response

import com.socialite.solite_pos.data.schema.room.EntityData

interface ResponseData {
    val id: String
    fun toEntity(): EntityData
}