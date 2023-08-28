package com.socialite.domain.domain

import com.socialite.data.schema.room.new_master.Category

fun interface UpdateCategory {
    suspend operator fun invoke(category: Category)
}