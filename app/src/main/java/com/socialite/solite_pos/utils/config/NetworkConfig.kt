package com.socialite.solite_pos.utils.config

import com.socialite.solite_pos.BuildConfig

object NetworkConfig {

    fun isDebugMode(): Boolean = BuildConfig.DEBUG

    fun getBaseUrl(): String = if (BuildConfig.DEBUG) {
        "http://10.0.2.2:8000/"
    } else {
        "https://denisyordanp.xyz"
    }
}