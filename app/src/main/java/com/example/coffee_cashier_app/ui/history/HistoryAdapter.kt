package com.example.coffee_cashier_app.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coffee_cashier_app.R
import com.example.coffee_cashier_app.model.Order

class HistoryAdapter(
    private var orders: List<Order> = emptyList(),
    // Добавим лямбду для обработки нажатия (если нужно, можно оставить пустой)
    private val onOrderClick: (Order) -> Unit = {}
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textDate: TextView = itemView.findViewById(R.id.textDateTime)
        val textComposition: TextView = itemView.findViewById(R.id.textItemsSummary)
        val textSum: TextView = itemView.findViewById(R.id.textSum)
        // Если в макете item_order.xml у вас уже есть поле для статуса, можно добавить его сюда
        // val textStatus: TextView = itemView.findViewById(R.id.textStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val order = orders[position]
        holder.textDate.text = order.dateTime
        // Формируем состав заказа, например: "Americano x1, Latte x2"
        val composition = order.items.joinToString(separator = ", ") { "${it.item.name} x${it.quantity}" }
        holder.textComposition.text = composition
        holder.textSum.text = "Сумма: ${order.total} сом"
        // Если хотите обрабатывать нажатие на заказ:
        holder.itemView.setOnClickListener { onOrderClick(order) }
    }

    override fun getItemCount(): Int = orders.size

    // Функция для обновления списка заказов
    fun updateList(newList: List<Order>) {
        orders = newList
        notifyDataSetChanged()
    }
}
