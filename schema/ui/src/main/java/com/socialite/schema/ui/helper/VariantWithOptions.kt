package com.socialite.schema.ui.helper

import com.socialite.schema.ui.main.Variant
import com.socialite.schema.ui.main.VariantOption

data class VariantWithOptions(
    val variant: Variant,
    val options: List<VariantOption>
) {
    fun optionsString() = options.joinToString { it.name }
    fun isOptionAvailable() = options.any { it.isActive }
}
