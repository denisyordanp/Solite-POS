package com.socialite.domain.domain.impl

import com.socialite.common.utility.di.DefaultDispatcher
import com.socialite.common.utility.state.DataState
import com.socialite.common.utility.state.ErrorState
import com.socialite.data.repository.SettingRepository
import com.socialite.domain.domain.IsAbleSendForgotPassword
import com.socialite.domain.helper.DateUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class IsAbleSendForgotPasswordImpl @Inject constructor(
    private val settingRepository: SettingRepository,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) : IsAbleSendForgotPassword {
    @OptIn(FlowPreview::class)
    override fun invoke(currentTime: Long) = settingRepository.getLastForgotPasswordTime()
        .flatMapConcat { lastTime ->
            val differenceTime = currentTime - lastTime
            if (lastTime != 0L && differenceTime <= DateUtils.THIRTY_MINUTES_IN_TIME_MILLIS) {
                val time = if (differenceTime > DateUtils.MINUTES_IN_TIME_MILLIS) {
                    val minutes = (differenceTime/DateUtils.MINUTES_IN_TIME_MILLIS).toInt()
                    "$minutes menit"
                } else {
                    val seconds = (differenceTime/DateUtils.SECOND_IN_TIME_MILLIS).toInt()
                    "$seconds detik"
                }
                flowOf(
                    DataState.Error(
                        ErrorState.LimitedSendForgotPassword(
                    additionalMessage = time,
                )))
            } else {
                flowOf(DataState.Success(true))
            }
        }.flowOn(dispatcher)
}