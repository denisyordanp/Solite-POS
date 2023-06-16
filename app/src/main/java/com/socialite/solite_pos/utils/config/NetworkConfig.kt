package com.socialite.solite_pos.utils.config

import com.socialite.solite_pos.BuildConfig

object NetworkConfig {

    fun isDebugMode(): Boolean = BuildConfig.DEBUG

    fun getBaseUrl(): String = "https://api-solite-release.denisyordanp.com/"

    fun timeout(): Long = 60
}
