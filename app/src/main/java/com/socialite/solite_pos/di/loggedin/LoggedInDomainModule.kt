package com.socialite.solite_pos.di.loggedin

import com.socialite.solite_pos.data.source.domain.GetOrdersGeneralMenuBadge
import com.socialite.solite_pos.data.source.domain.GetProductWithCategories
import com.socialite.solite_pos.data.source.domain.GetProductWithVariantOptions
import com.socialite.solite_pos.data.source.domain.MigrateToUUID
import com.socialite.solite_pos.data.source.domain.NewOrder
import com.socialite.solite_pos.data.source.domain.Synchronize
import com.socialite.solite_pos.data.source.domain.impl.GetOrdersGeneralMenuBadgeImpl
import com.socialite.solite_pos.data.source.domain.impl.GetProductWithCategoriesImpl
import com.socialite.solite_pos.data.source.domain.impl.GetProductWithVariantOptionsImpl
import com.socialite.solite_pos.data.source.domain.impl.MigrateToUUIDImpl
import com.socialite.solite_pos.data.source.domain.impl.NewOrderImpl
import com.socialite.solite_pos.data.source.domain.impl.SynchronizeImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class LoggedInDomainModule {

    @Binds
    @ViewModelScoped
    abstract fun bindGetOrdersGeneralMenuBadge(
        getOrdersGeneralMenuBadgeImpl: GetOrdersGeneralMenuBadgeImpl
    ): GetOrdersGeneralMenuBadge

    @Binds
    @ViewModelScoped
    abstract fun bindMigrateToUUID(
        migrateToUUIDImpl: MigrateToUUIDImpl
    ): MigrateToUUID

    @Binds
    @ViewModelScoped
    abstract fun bindSynchronize(
        synchronizeImpl: SynchronizeImpl
    ): Synchronize

    @Binds
    @ViewModelScoped
    abstract fun bindNewOrder(
        newOrderImpl: NewOrderImpl
    ): NewOrder

    @Binds
    @ViewModelScoped
    abstract fun bindGetProductWithCategories(
        getProductWithCategoriesImpl: GetProductWithCategoriesImpl
    ): GetProductWithCategories

    @Binds
    @ViewModelScoped
    abstract fun bindGetProductWithVariantOptions(
        getProductWithVariantOptions: GetProductWithVariantOptionsImpl
    ): GetProductWithVariantOptions
}
