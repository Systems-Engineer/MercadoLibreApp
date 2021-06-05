package com.brahian.mercadolibreapp.di

import com.brahian.mercadolibreapp.repository.MercadoLibreRepository
import com.brahian.mercadolibreapp.service.MercadoLibreAPIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

  @Singleton
  @Provides
  fun provideRepository(service: MercadoLibreAPIService): MercadoLibreRepository {
    return MercadoLibreRepository(service)
  }
}
