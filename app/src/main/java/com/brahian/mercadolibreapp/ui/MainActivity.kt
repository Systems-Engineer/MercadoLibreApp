package com.brahian.mercadolibreapp.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.brahian.mercadolibreapp.R
import com.brahian.mercadolibreapp.model.ProductSearchResponse
import com.brahian.mercadolibreapp.ui.view.ProductAdapter
import com.brahian.mercadolibreapp.util.DataState
import com.brahian.mercadolibreapp.viewmodel.MainStateEvent
import com.brahian.mercadolibreapp.viewmodel.MainViewModel
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private val viewModel: MainViewModel by viewModels()

  @Inject
  lateinit var glide : RequestManager

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    setObservers()
    viewModel.setStateEvent(MainStateEvent.GetProductEvents("Xiaomi Mi Band"))
  }

  private fun setObservers() {
    viewModel.dataState.observe(
      this,
      {
        when (it) {
          is DataState.Success -> {
            setLoading(false)
            setRecyclerView(it.data)
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

  private fun setRecyclerView(products: ProductSearchResponse) {
    recyclerview.apply {
      layoutManager = LinearLayoutManager(this@MainActivity)
      adapter = ProductAdapter(products.results ?: listOf(), glide) {
        DetailActivity.start(this@MainActivity, it)
      }
      addItemDecoration(
        DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL).apply {
          AppCompatResources.getDrawable(this@MainActivity, R.drawable.divider_layout)?.let { setDrawable(it) }
        }
      )
    }
  }

  private fun setError(message: String?) {
    // TODO add logic for errors
  }

  private fun setLoading(loading: Boolean) {
    progressbar.visibility = if (loading) View.VISIBLE else View.GONE
  }
}
