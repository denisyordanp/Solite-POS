package com.socialite.data.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthorizationService

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NonAuthorizationService
