package com.socialite.solite_pos.view

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.socialite.solite_pos.BuildConfig
import com.socialite.solite_pos.data.source.repository.impl.SettingRepositoryImpl
import com.socialite.solite_pos.utils.config.DateUtils
import com.socialite.solite_pos.view.viewModel.ApplicationViewModel
import java.util.Locale

class SoliteApp : Application() {

    private lateinit var viewModel: ApplicationViewModel

    override fun onCreate() {
        super.onCreate()

        setupViewModel()
        setupTheme()
        setupLocale()

        Firebase.crashlytics.setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
    }

    private fun setupViewModel() {
        val settingRepository = SettingRepositoryImpl.getDataStoreInstance(applicationContext)
        viewModel = ApplicationViewModel(this, settingRepository)
    }
    
    private fun setupTheme() {
        viewModel.getDarkMode {
            val delegate = if (it) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }

            AppCompatDelegate.setDefaultNightMode(delegate)
        }
    }

    private fun setupLocale() {
        val config = Configuration()
        config.setLocale(DateUtils.locale)
        Locale.setDefault(DateUtils.locale)
        val res = applicationContext.resources
        res.updateConfiguration(config, res.displayMetrics)
    }
}
