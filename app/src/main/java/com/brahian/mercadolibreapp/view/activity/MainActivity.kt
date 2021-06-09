package com.brahian.mercadolibreapp.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.brahian.mercadolibreapp.R
import com.brahian.mercadolibreapp.model.response.ProductSearchResponse
import com.brahian.mercadolibreapp.view.customview.ProductAdapter
import com.brahian.mercadolibreapp.viewmodel.util.DataState
import com.brahian.mercadolibreapp.viewmodel.viewmodel.MainStateEvent
import com.brahian.mercadolibreapp.viewmodel.viewmodel.MainViewModel
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

/**
 * Activity for searching for products and displaying them on a list
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private val viewModel: MainViewModel by viewModels()

  private val TAG = MainActivity::class.java.name

  @Inject
  lateinit var glide : RequestManager

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    setObservers()
    viewModel.setStateEvent(MainStateEvent.GetProduct(getString(R.string.tecnologia)))
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.options_menu, menu)
    (menu.findItem(R.id.search).actionView as SearchView).apply {
      queryHint = getString(R.string.search_title)
      setOnQueryTextListener(object : OnQueryTextListener {
        override fun onQueryTextChange(newText: String): Boolean {
          return false
        }
        override fun onQueryTextSubmit(query: String): Boolean {
          viewModel.setStateEvent(MainStateEvent.GetProduct(query, true))
          return false
        }
      })
    }
    return true
  }

  private fun setObservers() {
    viewModel.dataState.observe(
      this,
      {
        when (it) {
          is DataState.Success -> {
            setLoading(false)
            if (it.data.results.isNullOrEmpty()) {
              setError(getString(R.string.no_results_found))
            } else {
              setRecyclerView(it.data)
            }
          }
          is DataState.Error -> {
            setLoading(false)
            setError(it.exception.message, true)
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
          AppCompatResources.getDrawable(this@MainActivity, R.drawable.layout_divider)?.let { setDrawable(it) }
        }
      )
      visibility = VISIBLE
    }
  }

  private fun setError(message: String?, isException: Boolean = false) {
    if (isException) {
      Log.e(TAG, "setError: Got error while trying to fetch products: $message")
      textview_error.text = getString(R.string.error_message)
    } else {
      textview_error.text = message
    }
    layout_error.visibility = VISIBLE
    recyclerview.visibility = GONE
  }

  private fun setLoading(loading: Boolean) {
    if (loading) {
      progressbar.visibility = VISIBLE
      layout_error.visibility = GONE
    } else progressbar.visibility = GONE
  }

  companion object {
    fun start(context : Context) {
      val intent = Intent(context, MainActivity::class.java)
      context.startActivity(intent)
    }
  }
}
