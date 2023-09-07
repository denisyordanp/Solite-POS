package com.socialite.domain.domain.impl

import com.socialite.data.repository.SettingRepository
import com.socialite.domain.domain.SetNewPrinterAddress
import javax.inject.Inject

class SetNewPrinterAddressImpl @Inject constructor(
    private val settingRepository: SettingRepository
) : SetNewPrinterAddress {
    override fun invoke(address: String) {
        settingRepository.setPrinterDeviceAddress(address)
    }
}