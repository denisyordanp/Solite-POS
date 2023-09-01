package com.socialite.domain.domain

import com.socialite.domain.schema.main.Category

fun interface UpdateCategory {
    suspend operator fun invoke(category: Category)
}