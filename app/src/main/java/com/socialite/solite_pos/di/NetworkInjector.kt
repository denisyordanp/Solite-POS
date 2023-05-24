package com.socialite.solite_pos.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.socialite.solite_pos.data.source.preference.UserPreferences
import com.socialite.solite_pos.data.source.remote.SoliteServices
import com.socialite.solite_pos.utils.config.NetworkConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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

    private fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

    private fun provideOkHttp(
        config: NetworkConfig,
        loggingInterceptor: HttpLoggingInterceptor,
        userToken: String
    ): OkHttpClient.Builder =
        OkHttpClient.Builder().apply {
            if (config.isDebugMode()) {
                addNetworkInterceptor(loggingInterceptor)
            }
            if (userToken.isNotEmpty()) {
                addInterceptor(Interceptor { chain ->
                    val requestBuilder: okhttp3.Request.Builder = chain.request().newBuilder()
                    requestBuilder.header("Authorization", userToken)
                    return@Interceptor chain.proceed(requestBuilder.build())
                })
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

    fun provideSoliteServices(userPreferences: UserPreferences): SoliteServices {
        val config = NetworkConfig
        return provideRetrofit(
            config = config,
            gsonConverter = provideGsonConverter(),
            okHttpBuilder = provideOkHttp(
                config = config,
                loggingInterceptor = provideLoggingInterceptor(),
                userToken = userPreferences.getUserToken()
            )
        ).create(SoliteServices::class.java)
    }
}