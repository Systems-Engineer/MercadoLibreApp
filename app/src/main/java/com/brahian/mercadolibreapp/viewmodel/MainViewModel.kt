package com.brahian.mercadolibreapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brahian.mercadolibreapp.model.ProductSearchResponse
import com.brahian.mercadolibreapp.repository.MercadoLibreRepository
import com.brahian.mercadolibreapp.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class MainViewModel @Inject constructor(
  private val savedStateHandle: SavedStateHandle,
  private val mercadoLibreRepository: MercadoLibreRepository
) : ViewModel() {

  private val _dataState: MutableLiveData<DataState<ProductSearchResponse>> = MutableLiveData()
  val dataState: LiveData<DataState<ProductSearchResponse>>
    get() = _dataState

  fun setStateEvent(event: MainStateEvent) {
    viewModelScope.launch {
      when (event) {
        is MainStateEvent.GetProduct -> {
          if (dataState.value == null) {
            mercadoLibreRepository.getProducts(event.query).onEach {
              _dataState.value = it
            }.launchIn(viewModelScope)
          }
        }
      }
    }
  }
}

sealed class MainStateEvent {
  data class GetProduct(val query : String) : MainStateEvent()
}
