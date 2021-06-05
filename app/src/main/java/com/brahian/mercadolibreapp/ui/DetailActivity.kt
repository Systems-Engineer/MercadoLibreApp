package com.brahian.mercadolibreapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.brahian.mercadolibreapp.R
import com.brahian.mercadolibreapp.model.Product
import com.brahian.mercadolibreapp.util.DataState
import com.brahian.mercadolibreapp.viewmodel.DetailStateEvent
import com.brahian.mercadolibreapp.viewmodel.DetailViewModel
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_detail.*

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

  private val viewModel: DetailViewModel by viewModels()

  @Inject
  lateinit var glide : RequestManager

  private val TAG = DetailActivity::class.java.name

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_detail)

    setObservers()
    intent.getParcelableExtra<Product>(PRODUCT)?.let {
      viewModel.setStateEvent(DetailStateEvent.GetProductDetailEvent(it))
    } ?: run {
      finish()
      Log.e(TAG, "onCreate: Failed creating view due to having a null product as argument")
    }
  }

  private fun setObservers() {
    viewModel.dataState.observe(
      this,
      Observer {
        when (it) {
          is DataState.Success -> {
            setLoading(false)
            setDetail(it.data)
          }
          is DataState.Error -> {
            setLoading(false)
            setError(it.exception.message)
          }
          is DataState.Loading -> {
            setLoading(true)
          }
        }
      }
    )
  }

  private fun setDetail(product: Product) {
    glide.load(product.thumbnail).placeholder(R.drawable.ic_launcher_background).into(imageview_product)
    glide.load(product.seller?.eshop?.eshop_logo_url).placeholder(R.drawable.ic_launcher_background).into(imageview_eshop)
  }

  private fun setError(message: String?) {
    // no-op
  }

  private fun setLoading(loading: Boolean) {
    // no-op
  }

  companion object {
    private const val PRODUCT = "PRODUCT"

    fun start(context : Context, product: Product){
      val intent = Intent(context, DetailActivity::class.java)
      intent.putExtra(PRODUCT, product)
      context.startActivity(intent)
    }
  }
}
