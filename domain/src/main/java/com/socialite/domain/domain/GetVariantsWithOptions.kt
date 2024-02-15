package com.socialite.domain.domain

import com.socialite.schema.ui.helper.VariantWithOptions
import kotlinx.coroutines.flow.Flow

fun interface GetVariantsWithOptions {
    operator fun invoke(): Flow<List<VariantWithOptions>>
}
