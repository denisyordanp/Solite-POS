package com.socialite.solite_pos.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.socialite.solite_pos.data.preference.UserPreferences
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
    @AuthorizationService
    fun provideAuthorizationOkHttp(
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
    @NonAuthorizationService
    fun provideNonAuthorizationOkHttp(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient.Builder =
        OkHttpClient.Builder().apply {
            readTimeout(NetworkConfig.timeout(), TimeUnit.SECONDS)
            connectTimeout(NetworkConfig.timeout(), TimeUnit.SECONDS)
            if (NetworkConfig.isDebugMode()) {
                addNetworkInterceptor(loggingInterceptor)
            }
        }

    @Provides
    fun provideRetrofit(
        gsonConverter: GsonConverterFactory,
    ): Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl(NetworkConfig.getBaseUrl())
            .addConverterFactory(gsonConverter)

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
    @AuthorizationService
    fun provideAuthorizationSoliteServices(
        retrofit: Retrofit.Builder,
        @AuthorizationService okHttpBuilder: OkHttpClient.Builder
    ): SoliteServices {
        return retrofit
            .client(okHttpBuilder.build())
            .build().create(SoliteServices::class.java)
    }

    @Provides
    @NonAuthorizationService
    fun provideNonAuthorizationSoliteServices(
        retrofit: Retrofit.Builder,
        @NonAuthorizationService okHttpBuilder: OkHttpClient.Builder
    ): SoliteServices {
        return retrofit
            .client(okHttpBuilder.build())
            .build().create(SoliteServices::class.java)
    }
}
