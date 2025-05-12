package com.example.coffee_cashier_app.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coffee_cashier_app.R
import com.example.coffee_cashier_app.model.OrderResponseDto

class OrdersAdapter(
    private var orders: List<OrderResponseDto>,
    private val onOrderClick: (OrderResponseDto) -> Unit
) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    fun updateOrders(newOrders: List<OrderResponseDto>) {
        orders = newOrders
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.textDateTime.text    = order.orderDate
        holder.textItemsSummary.text = "${order.items.size} поз. на сумму ${order.finalAmount} сом"
        holder.textTotal.text       = "${order.finalAmount} сом"
        holder.itemView.setOnClickListener { onOrderClick(order) }
    }

    override fun getItemCount(): Int = orders.size

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textDateTime: TextView    = itemView.findViewById(R.id.textDateTime)
        val textItemsSummary: TextView = itemView.findViewById(R.id.textItemsSummary)
        val textTotal: TextView       = itemView.findViewById(R.id.textSum)
    }
}
