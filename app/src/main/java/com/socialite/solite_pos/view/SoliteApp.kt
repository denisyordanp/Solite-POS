package com.socialite.solite_pos.view

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.socialite.domain.domain.IsDarkModeActive
import com.socialite.common.utility.helper.DateUtils
import com.socialite.solite_pos.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltAndroidApp
class SoliteApp : Application() {

    @Inject
    lateinit var isDarkModeActive: IsDarkModeActive

    override fun onCreate() {
        super.onCreate()

        setupTheme()
        setupLocale()
        setupFirebase()
    }

    private fun setupTheme() {
        MainScope().launch {
            isDarkModeActive().first().apply {
                val delegate = if (this) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }

                AppCompatDelegate.setDefaultNightMode(delegate)
            }
        }
    }

    private fun setupLocale() {
        val config = Configuration()
        config.setLocale(DateUtils.locale)
        Locale.setDefault(DateUtils.locale)
        val res = applicationContext.resources
        res.updateConfiguration(config, res.displayMetrics)
    }

    private fun setupFirebase() {
        Firebase.crashlytics.setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
    }
}
