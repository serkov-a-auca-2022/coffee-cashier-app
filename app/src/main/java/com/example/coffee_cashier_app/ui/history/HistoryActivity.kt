package com.example.coffee_cashier_app.ui.history

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.coffee_cashier_app.data.Repository
import com.example.coffee_cashier_app.model.Order
import com.example.coffee_cashier_app.ui.detail.OrderDetailActivity
import com.example.coffee_cashier_app.ui.main.OrdersAdapter
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private lateinit var ordersAdapter: OrdersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ordersAdapter = OrdersAdapter { order ->
            val intent = Intent(this, OrderDetailActivity::class.java)
            intent.putExtra("order", order)
            startActivity(intent)
        }
        recyclerHistory.adapter = ordersAdapter

        progressHistory.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val historyList: List<Order> = Repository.getOrderHistory()
                ordersAdapter.setOrders(historyList)
            } catch (e: Exception) {
                Toast.makeText(
                    this@HistoryActivity,
                    "Ошибка загрузки истории",
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                progressHistory.visibility = View.GONE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
