package com.example.data_retrofit.services

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RealApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MockApi
