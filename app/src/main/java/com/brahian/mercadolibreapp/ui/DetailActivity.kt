package com.brahian.mercadolibreapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.brahian.mercadolibreapp.R
import com.brahian.mercadolibreapp.model.Attribute
import com.brahian.mercadolibreapp.model.Product
import com.brahian.mercadolibreapp.model.Seller
import com.brahian.mercadolibreapp.ui.view.AttributeAdapter
import com.brahian.mercadolibreapp.util.DataState.*
import com.brahian.mercadolibreapp.util.formatToCurrency
import com.brahian.mercadolibreapp.util.setTextAndCapitalize
import com.brahian.mercadolibreapp.viewmodel.DetailStateEvent
import com.brahian.mercadolibreapp.viewmodel.DetailViewModel
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.layout_seller_info.*
import java.util.*
import javax.inject.Inject

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
      viewModel.setStateEvent(DetailStateEvent.GetProductDetail(it))
      viewModel.setStateEvent(DetailStateEvent.GetSeller(it.seller?.id ?: ""))
    } ?: run {
      // TODO show snackbar error
      Log.e(TAG, "onCreate: Failed creating view due to having a null product as argument")
    }
  }

  private fun setObservers() {
    viewModel.productDataState.observe(
      this,
      {
        when (it) {
          is Success -> {
            setDetail(it.data)
          }
          else -> {
            // no-op
          }
        }
      }
    )
    viewModel.sellerDataState.observe(
      this,
      {
        when (it) {
          is Success -> {
            setSellerLoading(false)
            setSeller(it.data)
          }
          is Error -> {
            setSellerLoading(false)
            setSellerError(it.exception.message)
          }
          is Loading -> {
            setSellerLoading(true)
          }
        }
      }
    )
  }

  private fun setDetail(product: Product) {
    textview_condition_sold.setTextAndCapitalize(getString(R.string.condition_sold, product.condition, product.sold_quantity.toString()))
    textview_title.text = product.title
    textview_price.formatToCurrency(product.price)
    formatShippingAndMercadoPagoInfo(product)
    textview_stock.text = getString(R.string.stock, product.available_quantity.toString())
    glide.load(product.thumbnail).placeholder(R.drawable.ic_launcher_background).transition(
      DrawableTransitionOptions.withCrossFade()).into(imageview_product)
    setProductAttributes(product.attributes)
  }

  private fun setProductAttributes(attributes : List<Attribute>?) {
    recyclerview_attributes.apply {
      layoutManager = LinearLayoutManager(this@DetailActivity)
      adapter = AttributeAdapter(attributes ?: listOf())
      addItemDecoration(
        DividerItemDecoration(this@DetailActivity, DividerItemDecoration.VERTICAL).apply {
          AppCompatResources.getDrawable(this@DetailActivity, R.drawable.divider_layout)?.let { setDrawable(it) }
        }
      )
    }
  }

  private fun formatShippingAndMercadoPagoInfo(product : Product) {
    val shippingAndPago = StringBuilder()
    if (product.shipping?.free_shipping == true) {
      shippingAndPago.append(getString(
        if (product.accepts_mercadopago == true) R.string.free_shipping_mercadopago
        else R.string.free_shipping)
      )
    } else if (product.accepts_mercadopago == true) {
      shippingAndPago.append(getString(R.string.accepts_mercadopago))
    }
    if (shippingAndPago.toString().isEmpty() || shippingAndPago.toString().isBlank()) textview_shipping_pago.visibility = GONE
    else textview_shipping_pago.text = shippingAndPago.toString()
  }

  private fun setSeller(seller : Seller) {
    progressbar_seller.visibility = GONE
    imageview_eshop.visibility = VISIBLE
    glide.load(seller.logo).placeholder(R.mipmap.ic_launcher).transition(DrawableTransitionOptions.withCrossFade()).into(imageview_eshop)
    textview_seller_name.apply {
      text = seller.nickname
      visibility = VISIBLE
    }
    textview_seller_reputation.apply{
      setTextAndCapitalize(seller.seller_reputation?.power_seller_status)
      visibility = VISIBLE
    }
  }

  private fun setSellerError(message: String?) {
    // no-op
  }

  private fun setSellerLoading(loading: Boolean) {
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
