package com.brahian.mercadolibreapp.viewmodel.util

import java.lang.Exception

/**
 * Class for wrapping data in different States such as Success, Error and Loading - Perfect for MVI architecture
 */
sealed class DataState<out R> {

  data class Success<out T>(val data: T) : DataState<T>()
  data class Error(val exception: Exception) : DataState<Nothing>()
  object Loading : DataState<Nothing>()
}
