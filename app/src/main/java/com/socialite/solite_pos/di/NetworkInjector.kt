package com.socialite.solite_pos.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.socialite.solite_pos.data.source.remote.SoliteServices
import com.socialite.solite_pos.utils.config.NetworkConfig
import okhttp3.OkHttpClient
import retrofit2.Converter.Factory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkInjector {

    private fun provideGsonConverter(): Factory =
        GsonConverterFactory.create(
            GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
        )

    private fun provideRetrofit(config: NetworkConfig, gsonConverter: Factory): Retrofit =
        Retrofit.Builder()
            .baseUrl(config.getBaseUrl())
            .client(OkHttpClient())
            .addConverterFactory(gsonConverter)
            .build()

    fun provideSoliteServices(): SoliteServices =
        provideRetrofit(
            config = NetworkConfig,
            gsonConverter = provideGsonConverter()
        ).create(SoliteServices::class.java)
}