package com.socialite.solite_pos.data.source.local.entity.room.helper

import androidx.room.Embedded
import androidx.room.Relation
import com.socialite.solite_pos.data.source.local.entity.room.master.Variant
import com.socialite.solite_pos.data.source.local.entity.room.master.VariantOption
import java.io.Serializable

data class VariantWithOption(
    @Embedded val option: VariantOption,

    @Relation(
        parentColumn = Variant.ID,
        entityColumn = Variant.ID,
    ) val variant: Variant
) : Serializable
