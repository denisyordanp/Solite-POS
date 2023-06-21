package com.socialite.solite_pos.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.socialite.solite_pos.data.source.preference.UserPreferences
import com.socialite.solite_pos.data.source.remote.SoliteServices
import com.socialite.solite_pos.utils.config.NetworkConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideOkHttp(
        loggingInterceptor: HttpLoggingInterceptor,
        userPreferences: UserPreferences,
    ): OkHttpClient.Builder =
        OkHttpClient.Builder().apply {
            readTimeout(NetworkConfig.timeout(), TimeUnit.SECONDS)
            connectTimeout(NetworkConfig.timeout(), TimeUnit.SECONDS)
            if (NetworkConfig.isDebugMode()) {
                addNetworkInterceptor(loggingInterceptor)
            }
            val token = userPreferences.getUserToken()
            if (token.isNotEmpty()) {
                addInterceptor(Interceptor { chain ->
                    val requestBuilder: okhttp3.Request.Builder = chain.request().newBuilder()
                    requestBuilder.header("Authorization", token)
                    return@Interceptor chain.proceed(requestBuilder.build())
                })
            }
        }

    @Provides
    fun provideRetrofit(
        gsonConverter: GsonConverterFactory,
        okHttpBuilder: OkHttpClient.Builder
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(NetworkConfig.getBaseUrl())
            .client(okHttpBuilder.build())
            .addConverterFactory(gsonConverter)
            .build()

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

    @Provides
    fun provideGsonConverter(): GsonConverterFactory =
        GsonConverterFactory.create(
            GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
        )

    @Provides
    fun provideSoliteServices(
        retrofit: Retrofit
    ): SoliteServices {
        return retrofit.create(SoliteServices::class.java)
    }
}
