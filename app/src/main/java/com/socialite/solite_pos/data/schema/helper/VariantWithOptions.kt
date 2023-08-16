package com.socialite.solite_pos.data.schema.helper

import com.socialite.solite_pos.data.schema.room.new_master.Variant
import com.socialite.solite_pos.data.schema.room.new_master.VariantOption

data class VariantWithOptions(
    val variant: Variant,
    val options: List<VariantOption>
) {
    fun optionsString() = options.joinToString { it.name }
    fun isOptionAvailable() = options.any { it.isActive }
}
