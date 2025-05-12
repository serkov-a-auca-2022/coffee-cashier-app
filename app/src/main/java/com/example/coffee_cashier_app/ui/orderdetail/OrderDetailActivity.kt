package com.example.coffee_cashier_app.ui.orderdetail

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffee_cashier_app.databinding.ActivityOrderDetailBinding
import com.example.coffee_cashier_app.model.OrderResponseDto
import com.example.coffee_cashier_app.model.UserResponseDto
import com.example.coffee_cashier_app.repository.OrderRepository
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.coroutines.launch

class OrderDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderDetailBinding
    private var currentOrder: OrderResponseDto? = null

    private val scanLauncher = registerForActivityResult(ScanContract()) { result: ScanIntentResult? ->
        result?.contents?.let { qr ->
            loadUserInfo(qr)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        currentOrder = intent.getSerializableExtra("order") as? OrderResponseDto
        if (currentOrder == null) {
            Toast.makeText(this, "Ошибка: заказ не передан", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        binding.btnEnterQr.setOnClickListener {
            val qr = binding.editQrManual.text.toString().trim()
            if (qr.isNotEmpty()) loadUserInfo(qr)
            else Toast.makeText(this, "Введите номер QR", Toast.LENGTH_SHORT).show()
        }


        // Заполняем детали заказа
        currentOrder!!.let { order ->
            binding.textOrderNumber.text = "Заказ №${order.orderId}"
            binding.textDateTime  .text = order.orderDate
            binding.textSumDetail .text = "${order.finalAmount} сом"

            binding.recyclerOrderItems.layoutManager = LinearLayoutManager(this)
            binding.recyclerOrderItems.adapter       = OrderItemsAdapter(order.items)

            // Если сервер сразу вернул клиента в поле order.user — показываем сразу
            order.user?.let { user ->
                val fullName = listOfNotNull(user.firstName, user.lastName)
                    .joinToString(" ")
                binding.textUserInfo.text   = "Пользователь: $fullName"
                binding.textPoints.text     = "Баллы: ${user.points}"
                binding.textFreeDrinks.text = "Бесплатные напитки: ${user.freeDrinks}"
                // и можно скрыть кнопку сканирования, если хотите:
                // binding.btnScanQr.visibility = View.GONE
            }
        }

        // Кнопка сканирования QR
        binding.btnScanQr.setOnClickListener {
            val options = ScanOptions().apply {
                setPrompt("Наведите камеру на QR-код")
                setBeepEnabled(true)
                setOrientationLocked(false)
            }
            scanLauncher.launch(options)
        }

        // Завершить
        binding.btnCompleteOrder.setOnClickListener {
            binding.btnCompleteOrder.isEnabled = false
            lifecycleScope.launch {
                try {
                    OrderRepository.finishOrder(currentOrder!!.orderId.toInt())
                    Toast.makeText(this@OrderDetailActivity,
                        "Заказ завершён", Toast.LENGTH_SHORT).show()
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this@OrderDetailActivity,
                        "Ошибка: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                    binding.btnCompleteOrder.isEnabled = true
                }
            }
        }

        // Отменить
        binding.btnCancelOrder.setOnClickListener {
            lifecycleScope.launch {
                try {
                    OrderRepository.cancelOrder(currentOrder!!.orderId.toInt())
                    Toast.makeText(this@OrderDetailActivity,
                        "Заказ отменён", Toast.LENGTH_SHORT).show()
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this@OrderDetailActivity,
                        "Ошибка: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun loadUserInfo(qr: String) {
        lifecycleScope.launch {
            try {
                val user: UserResponseDto = OrderRepository.getUserByQr(qr)
                val fullName = listOfNotNull(user.firstName, user.lastName)
                    .joinToString(" ")
                binding.textUserInfo.text   = "Пользователь: $fullName"
                binding.textPoints.text     = "Баллы: ${user.points}"
                binding.textFreeDrinks.text = "Бесплатные напитки: ${user.freeDrinks}"
            } catch (e: Exception) {
                Toast.makeText(this@OrderDetailActivity,
                    "Пользователь не найден", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        if (item.itemId == android.R.id.home) {
            finish(); true
        } else super.onOptionsItemSelected(item)
}
