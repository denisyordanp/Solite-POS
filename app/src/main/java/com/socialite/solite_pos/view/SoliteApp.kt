package com.socialite.solite_pos.view

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.socialite.domain.domain.IsDarkModeActive
import com.socialite.domain.helper.DateUtils
import com.socialite.solite_pos.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale
import javax.inject.Inject

@HiltAndroidApp
class SoliteApp : Application() {

    @Inject
    lateinit var isDarkModeActive: IsDarkModeActive

    private lateinit var viewModel: ApplicationViewModel

    override fun onCreate() {
        super.onCreate()

        setupViewModel()
        setupTheme()
        setupLocale()
        setupFirebase()
    }

    private fun setupViewModel() {
        viewModel = ApplicationViewModel(this, isDarkModeActive)
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
