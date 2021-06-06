package com.brahian.mercadolibreapp.service

import com.brahian.mercadolibreapp.model.Currency
import com.brahian.mercadolibreapp.model.ProductSearchResponse
import com.brahian.mercadolibreapp.model.Seller
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MercadoLibreAPIService {

  @GET("sites/{site}/search")
  suspend fun getProducts(@Path("site") site : String, @Query("q") query : String): ProductSearchResponse

  @GET("currencies/{id}")
  suspend fun getCurrency(@Path("id") id : String) : Currency

  @GET("users/{id}")
  suspend fun getSeller(@Path("id") id : String) : Seller

}
