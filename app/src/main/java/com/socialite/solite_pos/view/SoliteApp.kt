package com.socialite.solite_pos.view

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.soloader.SoLoader
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

        setupFlipper()
        setupViewModel()
        setupTheme()
        setupLocale()
        setupFirebase()
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

    private fun setupFlipper() {
        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
            SoLoader.init(this, false)
            AndroidFlipperClient.getInstance(this).apply {
                addPlugin(InspectorFlipperPlugin(this@SoliteApp, DescriptorMapping.withDefaults()))
                addPlugin(DatabasesFlipperPlugin(this@SoliteApp))
                addPlugin(NetworkFlipperPlugin())
                start()
            }
        }
    }

    private fun setupFirebase() {
        Firebase.crashlytics.setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
    }
}
