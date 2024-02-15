package com.socialite.data.schema.response

import com.socialite.schema.database.EntityData

interface ResponseData {
    val id: String
    fun toEntity(): EntityData
}