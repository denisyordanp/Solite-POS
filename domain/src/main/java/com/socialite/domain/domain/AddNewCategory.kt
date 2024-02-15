package com.socialite.domain.domain

import com.socialite.schema.ui.main.Category

fun interface AddNewCategory {
    suspend operator fun invoke(category: Category)
}