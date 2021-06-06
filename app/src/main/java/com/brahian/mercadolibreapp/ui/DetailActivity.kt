package com.brahian.mercadolibreapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.brahian.mercadolibreapp.R
import com.brahian.mercadolibreapp.model.Currency
import com.brahian.mercadolibreapp.model.Product
import com.brahian.mercadolibreapp.model.Seller
import com.brahian.mercadolibreapp.util.DataState.*
import com.brahian.mercadolibreapp.viewmodel.DetailStateEvent
import com.brahian.mercadolibreapp.viewmodel.DetailViewModel
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_detail.*
import java.lang.StringBuilder

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
      viewModel.setStateEvent(DetailStateEvent.GetCurrency(it.currency_id ?: ""))
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
    viewModel.currencyDataState.observe(
            this,
            {
              when (it) {
                is Success -> {
                  setCurrencyLoading(false)
                  setCurrency(it.data)
                }
                is Error -> {
                  setCurrencyLoading(false)
                  setCurrencyError(it.exception.message)
                }
                is Loading -> {
                  setCurrencyLoading(true)
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
    textview_condition_sold.text = "${product.condition} | ${product.sold_quantity} sold"
    textview_title.text = product.title
    textview_price.text = "${product.currency_id}${product.price}"
    val shippingAndPago = StringBuilder()
    if (product.shipping?.free_shipping == true) {
      shippingAndPago.append("Free shipping")
      if (product.accepts_mercadopago == true) shippingAndPago.append(" | Accepts mercadopago")
    } else if (product.accepts_mercadopago == true) {
      shippingAndPago.append("Accepts mercadopago")
    }
    if (shippingAndPago.toString().isEmpty() || shippingAndPago.toString().isBlank()) textview_shipping_pago.visibility = GONE
    else textview_shipping_pago.text = shippingAndPago.toString()
    textview_stock.text = "Stock: ${product.available_quantity}"
    glide.load(product.thumbnail).placeholder(R.drawable.ic_launcher_background).into(imageview_product)

    // Seller info
    glide.load(R.drawable.ic_launcher_background).placeholder(R.drawable.ic_launcher_background).into(imageview_eshop)
    textview_seller_name.text = product.seller?.id
    textview_seller_address.text = "${product.seller_address?.city?.name} - ${product.seller_address?.state?.name}"
  }

  private fun setCurrency(currency : Currency) {

  }

  private fun setCurrencyError(message: String?) {
    // no-op
  }

  private fun setCurrencyLoading(loading: Boolean) {
    // no-op
  }

  private fun setSeller(seller : Seller) {

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
