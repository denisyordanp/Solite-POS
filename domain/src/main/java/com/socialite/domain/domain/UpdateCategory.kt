package com.socialite.domain.domain

import com.socialite.schema.ui.main.Category

fun interface UpdateCategory {
    suspend operator fun invoke(category: Category)
}