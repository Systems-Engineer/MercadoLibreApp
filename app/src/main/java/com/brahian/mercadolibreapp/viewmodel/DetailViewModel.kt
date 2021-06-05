package com.brahian.mercadolibreapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brahian.mercadolibreapp.model.Product
import com.brahian.mercadolibreapp.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
  private val savedStateHandle: SavedStateHandle
) : ViewModel() {

  private val _dataState: MutableLiveData<DataState<Product>> = MutableLiveData()
  val dataState: LiveData<DataState<Product>>
    get() = _dataState

  fun setStateEvent(state: DetailStateEvent) {
    viewModelScope.launch {
      when (state) {
        is DetailStateEvent.GetProductDetailEvent -> {
          if (_dataState.value == null) _dataState.postValue(DataState.Success(state.product))
        }
      }
    }
  }
}

sealed class DetailStateEvent {
  data class GetProductDetailEvent(val product : Product) : DetailStateEvent()
}