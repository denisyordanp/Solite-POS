package com.socialite.solite_pos.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.socialite.solite_pos.data.source.remote.SoliteServices
import com.socialite.solite_pos.utils.config.NetworkConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter.Factory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkInjector {

    fun provideGsonConverter(): Factory =
        GsonConverterFactory.create(
            GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
        )

    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

    private fun provideOkHttp(
        config: NetworkConfig,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient.Builder =
        OkHttpClient.Builder().apply {
            if (config.isDebugMode()) {
                addNetworkInterceptor(loggingInterceptor)
            }
        }

    private fun provideRetrofit(
        config: NetworkConfig,
        gsonConverter: Factory,
        okHttpBuilder: OkHttpClient.Builder
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(config.getBaseUrl())
            .client(okHttpBuilder.build())
            .addConverterFactory(gsonConverter)
            .build()

    fun provideSoliteServices(): SoliteServices {
        val config = NetworkConfig
        return provideRetrofit(
            config = config,
            gsonConverter = provideGsonConverter(),
            okHttpBuilder = provideOkHttp(
                config = config,
                loggingInterceptor = provideLoggingInterceptor()
            )
        ).create(SoliteServices::class.java)
    }
}