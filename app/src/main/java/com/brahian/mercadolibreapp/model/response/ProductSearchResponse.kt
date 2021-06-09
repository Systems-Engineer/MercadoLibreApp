package com.brahian.mercadolibreapp.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Data objects for BE responses related to products info
 */

@Parcelize
data class ProductSearchResponse (
  val results : List<Product>?
  ) : Parcelable

@Parcelize
data class Product (
  val id : String?,
  val title : String?,
  val price : Double?,
  val available_quantity: Int?,
  val sold_quantity : Int?,
  val condition : String?,
  val accepts_mercadopago : Boolean?,
  val seller_address : Address?,
  val shipping : Shipping?,
  val thumbnail : String?,
  val attributes : List<Attribute>?,
  val domain_id : String?,
  val seller : Seller?
  ) : Parcelable

@Parcelize
data class Address (
  val country : Country?,
  val state : State?,
  val city : City?
  ) : Parcelable

@Parcelize
data class Country (
  val name : String?
) : Parcelable

@Parcelize
data class State (
  val name : String?
) : Parcelable


@Parcelize
data class City (
  val name : String?
) : Parcelable

@Parcelize
data class Shipping (
  val free_shipping : Boolean?
  ) : Parcelable

@Parcelize
data class Attribute (
  val value_id : String?,
  val name : String?,
  val value_name : String?
  ) : Parcelable