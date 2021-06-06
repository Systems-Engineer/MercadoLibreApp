package com.brahian.mercadolibreapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Currency (
  val id : String?,
  val symbol : String?
) : Parcelable