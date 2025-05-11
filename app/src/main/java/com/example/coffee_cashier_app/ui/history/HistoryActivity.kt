package com.example.coffee_cashier_app.ui.history

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffee_cashier_app.databinding.ActivityHistoryBinding
import com.example.coffee_cashier_app.model.Order
import com.example.coffee_cashier_app.repository.OrderRepository
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private val fullList: MutableList<Order> = mutableListOf()
    // HistoryAdapter – убедитесь, что он реализован (см. OrdersAdapter в MainActivity как пример)
    private val adapter = HistoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.recyclerHistory.layoutManager = LinearLayoutManager(this)
        binding.recyclerHistory.adapter = adapter

        val statuses = listOf("Все", "Завершённые", "Отменённые")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statuses)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFilter.adapter = spinnerAdapter

        binding.spinnerFilter.setSelection(0)

        binding.spinnerFilter.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                // Здесь реализуйте фильтрацию списка по статусу (если нужно)
                adapter.updateList(fullList)
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }

        loadHistory()
    }

    private fun loadHistory() {
        lifecycleScope.launch {
            try {
                val orders = OrderRepository.getOrderHistory()
                fullList.clear()
                fullList.addAll(orders)
                adapter.updateList(fullList)
            } catch (e: Exception) {
                Toast.makeText(this@HistoryActivity, "Ошибка истории: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
