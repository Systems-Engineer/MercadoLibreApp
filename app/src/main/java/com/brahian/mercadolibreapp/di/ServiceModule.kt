package com.brahian.mercadolibreapp.di

import com.brahian.mercadolibreapp.service.MercadoLibreAPIService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

  @Singleton
  @Provides
  fun provideGsonBuilder(): Gson {
    return GsonBuilder().create()
  }

  @Singleton
  @Provides
  fun provideRetrofit(baseUrl: String, gson: Gson): Retrofit.Builder {
    return Retrofit.Builder()
      .baseUrl(baseUrl)
      .addConverterFactory(GsonConverterFactory.create(gson))
  }

  @Singleton
  @Provides
  fun provideAPIService(retrofit: Retrofit.Builder): MercadoLibreAPIService {
    return retrofit.build()
      .create(MercadoLibreAPIService::class.java)
  }
}
