package com.brahian.mercadolibreapp.di.module

import com.brahian.mercadolibreapp.model.repository.MercadoLibreRepository
import com.brahian.mercadolibreapp.model.service.MercadoLibreAPIService
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
