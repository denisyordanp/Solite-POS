package com.socialite.domain.domain.impl

import com.socialite.data.preference.SettingPreferences
import com.socialite.domain.domain.GetPrinterDevice
import javax.inject.Inject

class GetPrinterDeviceImpl @Inject constructor(
    private val settingPreferences: SettingPreferences
) : GetPrinterDevice {
    override fun invoke() = settingPreferences.printerDevice
}