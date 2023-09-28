package com.socialite.domain.domain.impl

import com.socialite.common.di.DefaultDispatcher
import com.socialite.common.extension.dataStateFlow
import com.socialite.common.state.DataState
import com.socialite.data.repository.AccountRepository
import com.socialite.data.repository.SettingRepository
import com.socialite.domain.domain.ForgotPassword
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ForgotPasswordImpl @Inject constructor(
    private val accountRepository: AccountRepository,
    private val settingRepository: SettingRepository,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) : ForgotPassword {
    @OptIn(FlowPreview::class)
    override fun invoke(email: String, currentTime: Long) = dataStateFlow(dispatcher) {
        accountRepository.forgotPassword(email)
    }.flatMapConcat {
        when (it) {
            is DataState.Error -> flowOf(DataState.Error(it.errorState))
            DataState.Idle -> flowOf(DataState.Idle)
            DataState.Loading -> flowOf(DataState.Loading)
            is DataState.Success -> flow<DataState<Boolean>> {
                settingRepository.setLastForgotPasswordTime(currentTime).collect()
                emit(DataState.Success(true))
            }
        }
    }
}