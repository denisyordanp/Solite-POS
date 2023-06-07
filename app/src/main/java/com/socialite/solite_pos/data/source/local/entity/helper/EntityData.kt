package com.socialite.solite_pos.data.source.local.entity.helper

interface EntityData {
    val id: String
    fun toResponse(): ResponseData
}