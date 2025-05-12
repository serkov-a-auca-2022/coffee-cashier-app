package com.example.coffee_cashier_app.ui.orderdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coffee_cashier_app.R
import com.example.coffee_cashier_app.model.OrderItemResponseDto

class OrderItemsAdapter(
    private val items: List<OrderItemResponseDto>
) : RecyclerView.Adapter<OrderItemsAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order_detail, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.nameText.text     = item.name
        holder.quantityText.text = "×${item.quantity}"
        holder.priceText.text    = "${item.price * item.quantity} сом"
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText:     TextView = view.findViewById(R.id.textProductName)
        val quantityText: TextView = view.findViewById(R.id.textProductQuantity)
        val priceText:    TextView = view.findViewById(R.id.textProductPrice)
    }
}
