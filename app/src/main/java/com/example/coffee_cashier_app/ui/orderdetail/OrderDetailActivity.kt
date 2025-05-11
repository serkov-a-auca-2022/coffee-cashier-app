package com.example.coffee_cashier_app.ui.orderdetail

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.coffee_cashier_app.databinding.ActivityOrderDetailBinding
import com.example.coffee_cashier_app.model.OrderResponseDto
import com.example.coffee_cashier_app.repository.OrderRepository
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import kotlinx.coroutines.launch

class OrderDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderDetailBinding
    private var currentOrder: OrderResponseDto? = null

    private val scanLauncher = registerForActivityResult(ScanContract()) { result: ScanIntentResult? ->
        result?.contents?.let { handleScannedQr(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Получаем заказ из интента
        currentOrder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("order", OrderResponseDto::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("order")
        }

        if (currentOrder == null) {
            Toast.makeText(this, "Ошибка: заказ не передан", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // Отображаем детали
        currentOrder!!.let { order ->
            binding.textDateTime.text     = "Заказ от ${order.orderDate}"
            binding.textOrderItems.text  = order.items.joinToString("\n") {
                "${it.quantity}× ${it.name} — ${it.price * it.quantity} сом"
            }
            binding.textSumDetail.text   = "${order.totalAmount} сом"
        }

        binding.btnScanQr.setOnClickListener    { scanLauncher.launch(null) }
        binding.btnCompleteOrder.setOnClickListener {
            currentOrder?.let { order ->
                binding.btnCompleteOrder.isEnabled = false
                lifecycleScope.launch {
                    try {
                        OrderRepository.finishOrder(order.orderId)
                        Toast.makeText(
                            this@OrderDetailActivity,
                            "Заказ завершён",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@OrderDetailActivity,
                            "Ошибка: ${e.localizedMessage}",
                            Toast.LENGTH_LONG
                        ).show()
                        binding.btnCompleteOrder.isEnabled = true
                    }
                }
            }
        }
        binding.btnCancelOrder.setOnClickListener {
            currentOrder?.let { order ->
                lifecycleScope.launch {
                    try {
                        OrderRepository.cancelOrder(order.orderId)
                        Toast.makeText(
                            this@OrderDetailActivity,
                            "Заказ отменён",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@OrderDetailActivity,
                            "Ошибка: ${e.localizedMessage}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun handleScannedQr(qr: String) {
        lifecycleScope.launch {
            try {
                val user = OrderRepository.getUserByQr(qr)
                binding.textUserInfo.text = "Пользователь: ${user.name}"
            } catch (_: Exception) {
                Toast.makeText(
                    this@OrderDetailActivity,
                    "Пользователь не найден",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        if (item.itemId == android.R.id.home) {
            finish()
            true
        } else super.onOptionsItemSelected(item)
}
