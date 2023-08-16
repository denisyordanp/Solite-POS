package com.socialite.data.network

import com.denisyordanp.data.BuildConfig

object NetworkConfig {

    fun isDebugMode(): Boolean = BuildConfig.DEBUG

    fun getBaseUrl(): String = if (isDebugMode())
        "https://api-solite-develop.denisyordanp.com/" else "https://api-solite-release.denisyordanp.com/"

    fun timeout(): Long = 60
}
