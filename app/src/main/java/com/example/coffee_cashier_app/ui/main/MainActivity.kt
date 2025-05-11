package com.example.coffee_cashier_app.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffee_cashier_app.databinding.ActivityMainBinding
import com.example.coffee_cashier_app.ui.createorder.CreateOrderActivity
import com.example.coffee_cashier_app.ui.history.HistoryActivity
import com.example.coffee_cashier_app.ui.orderdetail.OrderDetailActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var ordersAdapter: OrdersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ordersAdapter = OrdersAdapter { order ->
            val intent = Intent(this, OrderDetailActivity::class.java)
            intent.putExtra("order", order)
            startActivity(intent)
        }
        binding.recyclerOrders.layoutManager = LinearLayoutManager(this)
        binding.recyclerOrders.adapter = ordersAdapter

        binding.buttonCreateOrder.setOnClickListener {
            startActivity(Intent(this, CreateOrderActivity::class.java))
        }
        binding.buttonHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        mainViewModel.ordersLiveData.observe(this) { ordersList ->
            ordersAdapter.setOrders(ordersList)
        }
        mainViewModel.loadingLiveData.observe(this) { isLoading ->
            binding.progressLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        mainViewModel.loadOrders()
    }
}
