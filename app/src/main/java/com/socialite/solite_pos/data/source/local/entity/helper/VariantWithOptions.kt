package com.socialite.solite_pos.data.source.local.entity.helper

import com.socialite.solite_pos.data.source.local.entity.room.master.Variant
import com.socialite.solite_pos.data.source.local.entity.room.master.VariantOption

data class VariantWithOptions(
        var variant: Variant?,
        var options: ArrayList<VariantOption>
){
    constructor(): this(null, ArrayList())

    fun optionsString() = options.joinToString { it.name }
}
