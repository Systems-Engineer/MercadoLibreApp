package com.brahian.mercadolibreapp

import androidx.lifecycle.Observer
import com.brahian.mercadolibreapp.model.ProductSearchResponse
import com.brahian.mercadolibreapp.repository.MercadoLibreRepository
import com.brahian.mercadolibreapp.service.MercadoLibreAPIService
import com.brahian.mercadolibreapp.util.DataState
import com.brahian.mercadolibreapp.viewmodel.MainStateEvent
import com.brahian.mercadolibreapp.viewmodel.MainViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.*
import org.junit.Before
import org.junit.Test
import java.lang.Exception

/**
 * Unit tests para los MainViewModel
 */
@ExperimentalCoroutinesApi
class MainViewModelTest : BaseTest() {

  private lateinit var mainViewModel : MainViewModel

  @MockK
  private lateinit var api : MercadoLibreAPIService

  @MockK
  private lateinit var observer : Observer<DataState<ProductSearchResponse>>

  @Before
  fun setUp() {
    MockKAnnotations.init(this, relaxUnitFun = true)
    mainViewModel = MainViewModel(mockk(), MercadoLibreRepository(api))
    mainViewModel.dataState.observeForever(observer)
  }

  @Test
  fun `Verify VM throws Success when successfully fetching products`() {
    val query = "Audifonos"
    val data = ProductSearchResponse(
      listOf(mockk())
    )
    coEvery { api.getProducts("MCO", query) } returns data
    mainViewModel.setStateEvent(MainStateEvent.GetProduct(query))

    verify { observer.onChanged(DataState.Loading) }
    verify { observer.onChanged(DataState.Success(data)) }
  }

  @Test
  fun `Verify VM throws Error when getting error response`() {
    val query = "Audifonos"
    val exception = Exception("Oh no! An error occurred!")
    coEvery { api.getProducts("MCO", query) } throws exception
    mainViewModel.setStateEvent(MainStateEvent.GetProduct(query))

    verify { observer.onChanged(DataState.Loading) }
    verify { observer.onChanged(DataState.Error(exception)) }
  }

}
