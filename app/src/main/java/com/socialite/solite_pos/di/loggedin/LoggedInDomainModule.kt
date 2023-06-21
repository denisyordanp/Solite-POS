package com.socialite.solite_pos.di.loggedin

import com.socialite.solite_pos.data.source.domain.GetOrdersGeneralMenuBadge
import com.socialite.solite_pos.data.source.domain.MigrateToUUID
import com.socialite.solite_pos.data.source.domain.Synchronize
import com.socialite.solite_pos.data.source.domain.impl.GetOrdersGeneralMenuBadgeImpl
import com.socialite.solite_pos.data.source.domain.impl.MigrateToUUIDImpl
import com.socialite.solite_pos.data.source.domain.impl.SynchronizeImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LoggedInDomainModule {

    @Binds
    abstract fun bindGetOrdersGeneralMenuBadge(
        getOrdersGeneralMenuBadgeImpl: GetOrdersGeneralMenuBadgeImpl
    ): GetOrdersGeneralMenuBadge

    @Binds
    abstract fun bindMigrateToUUID(
        migrateToUUIDImpl: MigrateToUUIDImpl
    ): MigrateToUUID

    @Binds
    abstract fun bindSynchronize(
        synchronizeImpl: SynchronizeImpl
    ): Synchronize
}
