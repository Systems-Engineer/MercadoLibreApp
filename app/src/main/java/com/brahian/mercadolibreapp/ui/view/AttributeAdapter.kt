package com.brahian.mercadolibreapp.ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brahian.mercadolibreapp.R
import com.brahian.mercadolibreapp.model.Attribute

class AttributeAdapter(private val dataSet: List<Attribute>) :
  RecyclerView.Adapter<AttributeAdapter.ViewHolder>() {

  class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val name: TextView = view.findViewById(R.id.textview_name)
    val value: TextView = view.findViewById(R.id.textview_value)
  }

  override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(viewGroup.context)
      .inflate(R.layout.attribute_row_item, viewGroup, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
    viewHolder.apply {
      dataSet[position].let {
        name.text = viewHolder.itemView.context.getString(R.string.value_name, it.name)
        value.text = it.value_name
      }
    }
  }

  override fun getItemCount() = dataSet.size

}