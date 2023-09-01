package com.socialite.domain.domain

import com.socialite.domain.schema.main.Category

fun interface AddNewCategory {
    suspend operator fun invoke(category: Category)
}