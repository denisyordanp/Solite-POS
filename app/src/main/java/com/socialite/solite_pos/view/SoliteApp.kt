package com.socialite.solite_pos.view

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.socialite.solite_pos.BuildConfig
import com.socialite.solite_pos.di.RepositoryInjection
import com.socialite.solite_pos.utils.config.DateUtils
import com.socialite.solite_pos.view.viewModel.ApplicationViewModel
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale

@HiltAndroidApp
class SoliteApp : Application() {

    private lateinit var viewModel: ApplicationViewModel

    override fun onCreate() {
        super.onCreate()

        setupViewModel()
        setupTheme()
        setupLocale()
        setupFirebase()
    }

    private fun setupViewModel() {
        val settingRepository = RepositoryInjection.provideSettingRepository(this)
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

    private fun setupFirebase() {
        Firebase.crashlytics.setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
    }
}
