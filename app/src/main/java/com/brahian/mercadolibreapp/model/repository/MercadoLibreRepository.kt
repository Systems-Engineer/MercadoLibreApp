package com.brahian.mercadolibreapp.model.repository

import com.brahian.mercadolibreapp.model.response.ProductSearchResponse
import com.brahian.mercadolibreapp.model.response.Seller
import com.brahian.mercadolibreapp.model.service.MercadoLibreAPIService
import com.brahian.mercadolibreapp.viewmodel.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class MercadoLibreRepository @Inject constructor(
  private val mercadoLibreAPIService: MercadoLibreAPIService
) {

  private val countryId = "MCO"

  suspend fun getProducts(query: String) : Flow<DataState<ProductSearchResponse>> = flow {
    emit(DataState.Loading)
    try {
      emit(DataState.Success(mercadoLibreAPIService.getProducts(countryId, query)))
    } catch (e: Exception) {
      emit(DataState.Error(e))
    }
  }

  suspend fun getSeller(id : String) : Flow<DataState<Seller>> = flow {
    emit(DataState.Loading)
    try {
      emit(DataState.Success(mercadoLibreAPIService.getSeller(id)))
    } catch (e: Exception) {
      emit(DataState.Error(e))
    }
  }

}
