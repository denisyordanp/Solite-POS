package com.socialite.solite_pos.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.socialite.solite_pos.data.source.remote.SoliteServices
import okhttp3.OkHttpClient
import retrofit2.Converter.Factory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkInjector {

    private const val BASE_URL = "127.0.0.1:8000/"

    private fun provideGsonConverter(): Factory =
        GsonConverterFactory.create(
            GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
        )

    private fun provideRetrofit(gsonConverter: Factory): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient())
            .addConverterFactory(gsonConverter)
            .build()

    fun provideSoliteServices(
        retrofit: Retrofit,
        gsonConverter: Factory
    ): SoliteServices = retrofit.create(SoliteServices::class.java)
}