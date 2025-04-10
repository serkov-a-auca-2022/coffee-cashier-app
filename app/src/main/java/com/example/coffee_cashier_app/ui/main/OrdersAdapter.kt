package com.example.coffee_cashier_app.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coffee_cashier_app.model.Order

class OrdersAdapter(private val onOrderClick: (Order) -> Unit)
    : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    private var orders: List<Order> = emptyList()

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textDateTime: TextView = itemView.findViewById(R.id.textDateTime)
        val textItemsSummary: TextView = itemView.findViewById(R.id.textItemsSummary)
        val textTotal: TextView = itemView.findViewById(R.id.textTotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        // Bind order data to views
        holder.textDateTime.text = order.dateTime
        holder.textItemsSummary.text = if (order.items.isNotEmpty()) {
            val totalCount = order.items.sumOf { it.quantity }
            val firstItemName = order.items[0].item.name
            if (order.items.size == 1) {
                "${totalCount}× $firstItemName"
            } else {
                "${totalCount} товар(ов): $firstItemName и др."
            }
        } else {
            "Нет товаров"
        }
        holder.textTotal.text = "${order.total} сом"
        holder.itemView.setOnClickListener {
            onOrderClick(order)
        }
    }

    override fun getItemCount(): Int = orders.size

    fun setOrders(newOrders: List<Order>) {
        orders = newOrders
        notifyDataSetChanged()
    }
}
