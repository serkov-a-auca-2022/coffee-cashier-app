package com.example.coffee_cashier_app.ui.orderdetail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
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

    // Для списания бонусов
    private var freeUsed = 0
    private var freeMax = 0
    private var pointsMax = 0

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

        // 1) Получаем заказ из intent и показываем позиции
        currentOrder = intent.getSerializableExtra("order") as? OrderResponseDto
        binding.recyclerOrderItems.layoutManager = LinearLayoutManager(this)
        binding.recyclerOrderItems.adapter =
            OrderItemsAdapter(currentOrder?.items ?: emptyList())
        binding.textOrderNumber.text = "Заказ №${currentOrder?.orderId}"
        binding.textSumDetail.text   = "Сумма: ${currentOrder?.totalAmount}"

        // 2) Скрываем блок списания бонусов
        binding.loyaltyControls.visibility = View.GONE

        // 3) Если предзаказ пришёл с привязанным юзером — сразу показываем лояльти-блок
        currentOrder?.user?.let { user ->
            showLoyaltyControls(user, currentOrder!!)
        }

        // 4) Кнопка «Ввести» QR вручную
        binding.btnEnterQr.setOnClickListener {
            val qrText = binding.editQrManual.text.toString().trim()
            if (qrText.isEmpty()) {
                Toast.makeText(this, "Введите код QR", Toast.LENGTH_SHORT).show()
            } else {
                loadUserInfo(qrText)
            }
        }

        // 5) Кнопка «Сканировать QR»
        binding.btnScanQr.setOnClickListener {
            val options = ScanOptions().apply {
                setPrompt("Наведите камеру на QR-код")
                setBeepEnabled(true)
                setOrientationLocked(false)
            }
            scanLauncher.launch(options)
        }

        // 6) Кнопки +/– бесплатных напитков
        binding.btnPlusFree.setOnClickListener {
            if (freeUsed < freeMax) {
                freeUsed++
                binding.tvFreeCount.text = freeUsed.toString()
            } else {
                Toast.makeText(this, "Не больше $freeMax", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnMinusFree.setOnClickListener {
            if (freeUsed > 0) {
                freeUsed--
                binding.tvFreeCount.text = freeUsed.toString()
            }
        }

        // 7) «Завершить» с учётом бонусов
        binding.btnCompleteOrder.setOnClickListener {
            binding.btnCompleteOrder.isEnabled = false
            val ptsToUse = binding.etPoints.text.toString().toIntOrNull() ?: 0

            lifecycleScope.launch {
                try {
                    // вызываем нужный endpoint
                    val updatedOrder = if (freeUsed > 0 || ptsToUse > 0) {
                        OrderRepository.checkoutWithRewards(
                            currentOrder!!.orderId.toInt(),
                            freeUsed,
                            ptsToUse
                        )
                    } else {
                        OrderRepository.finishOrder(currentOrder!!.orderId.toInt())
                    }

                    // 8) Обновляем UI под новый заказ
                    currentOrder = updatedOrder
                    binding.textSumDetail.text   = "Итог: ${updatedOrder.finalAmount}"
                    binding.textPoints.text      = "Баллы: ${updatedOrder.user?.points}"
                    binding.textFreeDrinks.text  = "Бесплатные напитки: ${updatedOrder.user?.freeDrinks}"

                    Toast.makeText(
                        this@OrderDetailActivity,
                        "Заказ №${updatedOrder.orderId} завершён",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()

                } catch (e: Exception) {
                    binding.btnCompleteOrder.isEnabled = true
                    Toast.makeText(
                        this@OrderDetailActivity,
                        "Ошибка: ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        // 9) Отмена заказа
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

    /**
     * Получаем пользователя по QR и привязываем его к заказу, затем показываем лояльти-блок
     */
    private fun loadUserInfo(qr: String) {
        lifecycleScope.launch {
            try {
                val user = OrderRepository.getUserByQr(qr)
                currentOrder = OrderRepository.assignUser(
                    currentOrder!!.orderId.toInt(),
                    user.id
                )
                showLoyaltyControls(user, currentOrder!!)
            } catch (e: Exception) {
                Toast.makeText(this@OrderDetailActivity,
                    "Пользователь не найден", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Инициализация блока списания бонусов
     */
    private fun showLoyaltyControls(user: UserResponseDto, order: OrderResponseDto) {
        // Обновляем данные пользователя
        val fullName = listOfNotNull(user.firstName, user.lastName).joinToString(" ")
        binding.textUserInfo.text    = "Пользователь: $fullName"
        binding.textPoints.text      = "Баллы: ${user.points}"
        binding.textFreeDrinks.text  = "Бесплатные напитки: ${user.freeDrinks}"

        // Считаем, сколько напитков в заказе (по количеству)
        val drinksInOrder = order.items.sumOf { it.quantity }
        freeMax   = minOf(user.freeDrinks, drinksInOrder)
        pointsMax = minOf(user.points.toInt(), order.finalAmount.toInt())

        // Сбрасываем счётчики
        freeUsed = 0
        binding.tvFreeCount.text = "0"
        binding.etPoints.hint    = "Макс $pointsMax"

        binding.loyaltyControls.visibility = View.VISIBLE
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        if (item.itemId == android.R.id.home) {
            finish(); true
        } else super.onOptionsItemSelected(item)
}
