package com.socialite.data.schema.response

import com.socialite.data.schema.room.EntityData

interface ResponseData {
    val id: String
    fun toEntity(): EntityData
}