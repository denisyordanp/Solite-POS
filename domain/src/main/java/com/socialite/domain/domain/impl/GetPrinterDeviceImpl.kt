package com.socialite.domain.domain.impl

import com.socialite.data.repository.SettingRepository
import com.socialite.domain.domain.GetPrinterDevice
import javax.inject.Inject

class GetPrinterDeviceImpl @Inject constructor(
    private val settingRepository: SettingRepository
) : GetPrinterDevice {
    override fun invoke() = settingRepository.getPrinterDeviceAddress()
}