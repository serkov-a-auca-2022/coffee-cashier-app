package com.example.coffee_cashier_app.ui.history

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coffee_cashier_app.R
import com.example.coffee_cashier_app.model.OrderResponseDto

class HistoryAdapter(
    private var orders: List<OrderResponseDto>,
    private val onOrderClick: (OrderResponseDto) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    fun updateOrders(newOrders: List<OrderResponseDto>) {
        orders = newOrders
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val order = orders[position]

        holder.textOrderNumber.text = "Заказ №${order.orderId}"
        holder.textOrderDate  .text = order.orderDate
        holder.textOrderStatus.text = when (order.status) {
            "FINISHED"  -> "Завершён"
            "CANCELLED" -> "Отменён"
            else        -> order.status
        }

        // выводим имя клиента или "Гость"
        holder.textUserName.text = order.user
            ?.let { "${it.firstName.orEmpty()} ${it.lastName.orEmpty()}" }
            ?: "Гость"

        // 2) Суммы
        holder.textOriginalSum.apply {
            text = "Сумма: ${order.totalAmount}"
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
        holder.textFinalSum.text = "Итог: ${order.finalAmount}"

        // 3) Баллы и бесплатные напитки
        holder.textPointsUsed.text = "Списано баллов: ${order.pointsUsed}"
        holder.textFreeUsed.text   = "Списано бесплатных напитков: ${order.freeDrinksUsedCount}"

        holder.itemView.setOnClickListener { onOrderClick(order) }
    }

    override fun getItemCount(): Int = orders.size

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textOrderNumber: TextView = itemView.findViewById(R.id.textOrderNumber)
        val textOrderDate:   TextView = itemView.findViewById(R.id.textOrderDate)
        val textOrderStatus: TextView = itemView.findViewById(R.id.textOrderStatus)
        val textUserName:    TextView = itemView.findViewById(R.id.textUserName)
        val textOriginalSum: TextView = itemView.findViewById(R.id.textOriginalSum)
        val textFinalSum:    TextView = itemView.findViewById(R.id.textFinalSum)
        val textPointsUsed:  TextView = itemView.findViewById(R.id.textPointsUsed)
        val textFreeUsed:    TextView = itemView.findViewById(R.id.textFreeUsed)
    }
}
