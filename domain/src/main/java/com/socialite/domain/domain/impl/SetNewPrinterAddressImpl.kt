package com.socialite.domain.domain.impl

import com.socialite.data.preference.SettingPreferences
import com.socialite.domain.domain.SetNewPrinterAddress
import javax.inject.Inject

class SetNewPrinterAddressImpl @Inject constructor(
    private val settingPreferences: SettingPreferences
) : SetNewPrinterAddress {
    override fun invoke(address: String) {
        settingPreferences.printerDevice = address
    }
}