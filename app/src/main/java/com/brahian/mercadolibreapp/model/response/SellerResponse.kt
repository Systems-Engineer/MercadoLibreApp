package com.brahian.mercadolibreapp.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Data objects for BE responses related to seller info
 */

@Parcelize
data class Seller (
  val id : String?,
  val nickname : String?,
  val logo : String?,
  val seller_reputation : Reputation?
) : Parcelable

@Parcelize
data class Reputation (
  val power_seller_status : String?,
  val transactions : Transaction?
) : Parcelable

@Parcelize
data class Transaction (
  val ratings : Rating?
) : Parcelable

@Parcelize
data class Rating (
  val negative : Double?,
  val neutral : Double?,
  val positive : Double?
) : Parcelable