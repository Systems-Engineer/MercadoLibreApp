package com.brahian.mercadolibreapp.service

import com.brahian.mercadolibreapp.model.ProductSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MercadoLibreAPIService {

  @GET("sites/{site}/search")
  suspend fun getProducts(@Path("site") site : String, @Query("q") query : String): ProductSearchResponse
}
