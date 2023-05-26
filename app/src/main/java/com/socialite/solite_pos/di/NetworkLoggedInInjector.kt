package com.socialite.solite_pos.di

import com.socialite.solite_pos.data.source.preference.UserPreferences
import com.socialite.solite_pos.data.source.remote.SoliteServices
import com.socialite.solite_pos.utils.config.NetworkConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter.Factory
import retrofit2.Retrofit

object NetworkLoggedInInjector {

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
        val token = userPreferences.getUserToken()
        return provideRetrofit(
            config = config,
            gsonConverter = NetworkInjector.provideGsonConverter(),
            okHttpBuilder = provideOkHttp(
                config = config,
                loggingInterceptor = NetworkInjector.provideLoggingInterceptor(),
                userToken = token
            )
        ).create(SoliteServices::class.java)
    }
}