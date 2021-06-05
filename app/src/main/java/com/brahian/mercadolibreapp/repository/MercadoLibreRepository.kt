package com.brahian.mercadolibreapp.repository

import com.brahian.mercadolibreapp.model.ProductSearchResponse
import com.brahian.mercadolibreapp.service.MercadoLibreAPIService
import com.brahian.mercadolibreapp.util.DataState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class MercadoLibreRepository @Inject constructor(
  private val mercadoLibreAPIService: MercadoLibreAPIService
) {

  suspend fun getProducts(query: String): Flow<DataState<ProductSearchResponse>> = flow {
    emit(DataState.Loading)
    try {
      delay(1000)
      emit(DataState.Success(mercadoLibreAPIService.getProducts("MCO", query)))
    } catch (e: Exception) {
      emit(DataState.Error(e))
    }
  }
}
