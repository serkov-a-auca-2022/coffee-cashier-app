package com.example.coffee_cashier_app.ui.history

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffee_cashier_app.databinding.ActivityHistoryBinding
import com.example.coffee_cashier_app.repository.OrderRepository
import com.example.coffee_cashier_app.ui.orderdetail.OrderDetailActivity
import kotlinx.coroutines.launch


class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = HistoryAdapter(emptyList()) { order ->
            val intent = Intent(this, OrderDetailActivity::class.java)
            intent.putExtra("order", order)
            startActivity(intent)
        }
        binding.recyclerHistory.layoutManager = LinearLayoutManager(this)
        binding.recyclerHistory.adapter = adapter

        loadHistory()
    }

    private fun loadHistory() {
        lifecycleScope.launch {
            try {
                val list = OrderRepository.getOrderHistory()
                adapter.updateOrders(list)
            } catch (e: Exception) {
                Toast.makeText(
                    this@HistoryActivity,
                    "Ошибка истории: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
