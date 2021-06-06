package com.brahian.mercadolibreapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brahian.mercadolibreapp.model.Product
import com.brahian.mercadolibreapp.model.Seller
import com.brahian.mercadolibreapp.repository.MercadoLibreRepository
import com.brahian.mercadolibreapp.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
  private val savedStateHandle: SavedStateHandle,
  private val mercadoLibreRepository: MercadoLibreRepository
) : ViewModel() {

  private val _productDataState: MutableLiveData<DataState<Product>> = MutableLiveData()
  val productDataState: LiveData<DataState<Product>>
    get() = _productDataState

  private val _sellerDataState: MutableLiveData<DataState<Seller>> = MutableLiveData()
  val sellerDataState: LiveData<DataState<Seller>>
    get() = _sellerDataState

  fun setStateEvent(event: DetailStateEvent) {
    viewModelScope.launch {
      when (event) {
        is DetailStateEvent.GetProductDetail -> {
          if (_productDataState.value == null) _productDataState.postValue(DataState.Success(event.product))
        }
        is DetailStateEvent.GetSeller -> {
          if (_sellerDataState.value == null) {
            mercadoLibreRepository.getSeller(event.sellerId).onEach {
              _sellerDataState.value = it
            }.launchIn(viewModelScope)
          }
        }
      }
    }
  }
}

sealed class DetailStateEvent {
  data class GetProductDetail(val product : Product) : DetailStateEvent()
  data class GetSeller(val sellerId : String) : DetailStateEvent()
}