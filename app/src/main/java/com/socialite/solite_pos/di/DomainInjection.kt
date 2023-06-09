package com.socialite.solite_pos.di

import android.content.Context
import com.socialite.solite_pos.data.source.domain.LoginUser
import com.socialite.solite_pos.data.source.domain.RegisterUser
import com.socialite.solite_pos.data.source.domain.impl.LoginUserImpl
import com.socialite.solite_pos.data.source.domain.impl.RegisterUserImpl

object DomainInjection {

    fun provideLoginUser(context: Context): LoginUser {
        return LoginUserImpl(
            repository = RepositoryInjection.provideUserRepository(),
            settingRepository = RepositoryInjection.provideSettingRepository(context)
        )
    }

    fun provideRegisterUser(context: Context): RegisterUser {
        return RegisterUserImpl(
            repository = RepositoryInjection.provideUserRepository(),
            settingRepository = RepositoryInjection.provideSettingRepository(context)
        )
    }
}
