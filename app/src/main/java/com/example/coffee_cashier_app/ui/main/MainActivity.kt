package com.example.coffee_cashier_app.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.coffee_cashier_app.ui.create.CreateOrderActivity
import com.example.coffee_cashier_app.ui.detail.OrderDetailActivity
import com.example.coffee_cashier_app.ui.history.HistoryActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var ordersAdapter: OrdersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup RecyclerView and adapter
        ordersAdapter = OrdersAdapter { order ->
            val intent = Intent(this, OrderDetailActivity::class.java)
            intent.putExtra("order", order)
            startActivity(intent)
        }
        recyclerOrders.adapter = ordersAdapter

        buttonCreateOrder.setOnClickListener {
            startActivity(Intent(this, CreateOrderActivity::class.java))
        }
        buttonHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        // Observe ViewModel LiveData
        mainViewModel.ordersLiveData.observe(this) { ordersList ->
            ordersAdapter.setOrders(ordersList)
        }
        mainViewModel.loadingLiveData.observe(this) { isLoading ->
            progressLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Load orders list from server
        mainViewModel.loadOrders()
    }
}
