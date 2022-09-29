package com.socialite.solite_pos.utils.di

import android.content.Context
import com.socialite.solite_pos.data.source.domain.GetProductVariantOptions
import com.socialite.solite_pos.data.source.domain.impl.GetProductVariantOptionsImpl
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.getInstance

object DomainInjection {
    fun provideGetProductVariantOptions(context: Context): GetProductVariantOptions {
        val database = getInstance(context)
        return GetProductVariantOptionsImpl(database.productVariantsDao())
    }
}
