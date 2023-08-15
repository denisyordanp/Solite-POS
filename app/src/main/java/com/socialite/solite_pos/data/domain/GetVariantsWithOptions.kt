package com.socialite.solite_pos.data.domain

import com.socialite.solite_pos.data.source.local.entity.helper.VariantWithOptions
import kotlinx.coroutines.flow.Flow

fun interface GetVariantsWithOptions {
    operator fun invoke(): Flow<List<VariantWithOptions>>
}
