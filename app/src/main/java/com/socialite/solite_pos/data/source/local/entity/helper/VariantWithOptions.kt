package com.socialite.solite_pos.data.source.local.entity.helper

import com.socialite.solite_pos.data.source.local.entity.room.master.Variant
import com.socialite.solite_pos.data.source.local.entity.room.master.VariantOption

data class VariantWithOptions(
        val variant: Variant?,
        val options: List<VariantOption>
){

    fun optionsString() = options.joinToString { it.name }
}
