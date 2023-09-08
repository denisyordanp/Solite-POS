package com.socialite.data.network

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.socialite.data.BuildConfig

object NetworkConfig {

    fun isDebugMode(): Boolean = BuildConfig.DEBUG

    fun getBaseUrl(): String = BuildConfig.BASE_URL

    fun timeout(): Long = 60

    fun gson(): Gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()
}
