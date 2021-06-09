package com.brahian.mercadolibreapp.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BaseUrlModule {

  @Provides
  @Singleton
  fun provideUrl(): String = "https://api.mercadolibre.com/"
}
