package com.socialite.solite_pos.data.source.local.entity.helper

interface ResponseData {
    val id: String
    fun toEntity(): EntityData
}