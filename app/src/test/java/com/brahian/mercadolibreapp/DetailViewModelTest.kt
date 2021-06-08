package com.brahian.mercadolibreapp

import androidx.lifecycle.Observer
import com.brahian.mercadolibreapp.model.Product
import com.brahian.mercadolibreapp.model.ProductSearchResponse
import com.brahian.mercadolibreapp.model.Seller
import com.brahian.mercadolibreapp.repository.MercadoLibreRepository
import com.brahian.mercadolibreapp.service.MercadoLibreAPIService
import com.brahian.mercadolibreapp.util.DataState
import com.brahian.mercadolibreapp.viewmodel.DetailStateEvent
import com.brahian.mercadolibreapp.viewmodel.DetailViewModel
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.*
import org.junit.Before
import org.junit.Test
import java.lang.Exception

/**
 * Unit tests para los DetailViewModel
 */
@ExperimentalCoroutinesApi
class DetailViewModelTest : BaseTest() {

  private lateinit var detailViewModel : DetailViewModel

  @MockK
  private lateinit var api : MercadoLibreAPIService

  @MockK
  private lateinit var productObserver : Observer<DataState<Product>>

  @MockK
  private lateinit var sellerObserver : Observer<DataState<Seller>>

  @Before
  fun setUp() {
    MockKAnnotations.init(this, relaxUnitFun = true)
    detailViewModel = DetailViewModel(mockk(), MercadoLibreRepository(api))
    detailViewModel.productDataState.observeForever(productObserver)
    detailViewModel.sellerDataState.observeForever(sellerObserver)
  }

  @Test
  fun `Verify VM throws Success when successfully receiving product detail data`() {
    val product = mockk<Product>()
    detailViewModel.setStateEvent(DetailStateEvent.GetProductDetail(product))

    verifyOrder {
      productObserver.onChanged(DataState.Success(product))
    }
  }

  @Test
  fun `Verify VM throws Success when successfully fetching seller info`() {
    val seller = mockk<Seller>()
    val id = "SELLER_ID"
    coEvery { api.getSeller(id) } returns seller

    detailViewModel.setStateEvent(DetailStateEvent.GetSeller(id))
    verifyOrder {
      sellerObserver.onChanged(DataState.Loading)
      sellerObserver.onChanged(DataState.Success(seller))
    }
  }

  @Test
  fun `Verify VM throws Error when getting error response while fetching for seller info`() {
    val exception = Exception("Oh no! An error occurred!")
    val id = "SELLER_ID"
    coEvery { api.getSeller(id) } throws exception

    detailViewModel.setStateEvent(DetailStateEvent.GetSeller(id))
    verifyOrder {
      sellerObserver.onChanged(DataState.Loading)
      sellerObserver.onChanged(DataState.Error(exception))
    }
  }

}
