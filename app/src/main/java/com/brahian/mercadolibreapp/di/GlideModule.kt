package com.brahian.mercadolibreapp.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object GlideModule {

  @Provides
  fun provideGlideRequestManager(@ApplicationContext context: Context): RequestManager {
    return Glide.with(context)
  }
}
