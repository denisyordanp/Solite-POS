package com.socialite.domain.domain

import com.socialite.domain.schema.helper.VariantWithOptions
import kotlinx.coroutines.flow.Flow

fun interface GetVariantsWithOptions {
    operator fun invoke(): Flow<List<VariantWithOptions>>
}
