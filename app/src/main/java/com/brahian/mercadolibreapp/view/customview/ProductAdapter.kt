package com.brahian.mercadolibreapp.view.customview

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.brahian.mercadolibreapp.R
import com.brahian.mercadolibreapp.model.response.Product
import com.brahian.mercadolibreapp.view.util.formatToCurrency
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

/**
 * Adapter for displaying products on RecyclerView
 */
class ProductAdapter(private val dataSet: List<Product>, private val glide : RequestManager, private val onTap : (Product)-> Unit) :
  RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

  class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val row: ConstraintLayout = view.findViewById(R.id.layout_row)
    val image: ImageView = view.findViewById(R.id.imageview)
    val title: TextView = view.findViewById(R.id.textview_title)
    val price: TextView = view.findViewById(R.id.textview_price)
    val shipping: TextView = view.findViewById(R.id.textview_shipping)
    val location: TextView = view.findViewById(R.id.textview_location)
  }

  override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(viewGroup.context)
      .inflate(R.layout.product_row_item, viewGroup, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
    viewHolder.apply {
      val context = viewHolder.itemView.context
      dataSet[position].let {
        glide.load(it.thumbnail).placeholder(R.drawable.ic_launcher_background).transition(DrawableTransitionOptions.withCrossFade()).into(image)
        title.text = it.title
        price.formatToCurrency(it.price)
        if (it.shipping?.free_shipping == true) {
          shipping.text = context.getString(R.string.free_shipping)
        } else shipping.visibility = GONE
        location.text = context.getString(R.string.address, it.seller_address?.city?.name, it.seller_address?.state?.name)
        row.setOnClickListener {
          onTap.invoke(dataSet[position])
        }
      }
    }
  }

  override fun getItemCount() = dataSet.size

}