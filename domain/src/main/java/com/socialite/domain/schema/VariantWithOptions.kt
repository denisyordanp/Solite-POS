package com.socialite.domain.schema

import com.socialite.domain.schema.main.Variant
import com.socialite.domain.schema.main.VariantOption

data class VariantWithOptions(
    val variant: Variant,
    val options: List<VariantOption>
) {
    fun optionsString() = options.joinToString { it.name }
    fun isOptionAvailable() = options.any { it.isActive }
}
