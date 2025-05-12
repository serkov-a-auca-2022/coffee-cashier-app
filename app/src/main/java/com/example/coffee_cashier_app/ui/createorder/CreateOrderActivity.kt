package com.example.coffee_cashier_app.ui.createorder

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffee_cashier_app.databinding.ActivityCreateOrderBinding
import com.example.coffee_cashier_app.model.Category
import com.example.coffee_cashier_app.model.Item
import com.example.coffee_cashier_app.network.OrderItemDto
import com.example.coffee_cashier_app.repository.OrderRepository
import com.example.coffee_cashier_app.repository.ProductRepository
import com.example.coffee_cashier_app.ui.main.MainActivity
import kotlinx.coroutines.launch

class CreateOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateOrderBinding
    private val cart = mutableMapOf<Item, Int>()

    private val cartAdapter: CartAdapter by lazy {
        CartAdapter(cart) { item, newQty ->
            if (newQty <= 0) cart.remove(item)
            else            cart[item] = newQty
            cartAdapter.notifyDataSetChanged()
            updateTotal()
            updateConfirmState()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // инициализируем списки
        binding.rvCategories.layoutManager = LinearLayoutManager(this)
        binding.rvProducts.layoutManager   = LinearLayoutManager(this)
        binding.rvCart.layoutManager       = LinearLayoutManager(this)
        binding.rvProducts.visibility      = View.GONE
        binding.rvCart.adapter             = cartAdapter
        updateConfirmState()

        // грузим товары и категории
        lifecycleScope.launch {
            val all   = ProductRepository.getAll()
            val byCat = all.groupBy { it.category }
            val cats  = byCat.keys.map { Category(it) }

            binding.rvCategories.adapter = CategoryAdapter(cats) { cat ->
                // при выборе категории показываем список товаров
                binding.rvCategories.visibility = View.GONE
                binding.rvProducts.visibility   = View.VISIBLE

                val prodAdapter = ProductAdapter { item ->
                    val newQty = (cart[item] ?: 0) + 1
                    cart[item] = newQty
                    cartAdapter.notifyDataSetChanged()
                    updateTotal()
                    updateConfirmState()
                }

                binding.rvProducts.adapter = prodAdapter
                prodAdapter.submitList(byCat[cat.name]!!)
            }
        }

        // отмена заказа (возвращаемся на главный экран)
        binding.btnCancelOrder.setOnClickListener { finish() }

        // подтверждение заказа
        binding.btnConfirmOrder.setOnClickListener {
            binding.btnConfirmOrder.isEnabled   = false
            binding.progressCreating.visibility = View.VISIBLE

            lifecycleScope.launch {
                try {
                    val itemsDto = cart.map { (item, qty) ->
                        OrderItemDto(
                            productId = item.id.toLong(),  // Long!
                            quantity  = qty
                        )
                    }
                    OrderRepository.createOrder(itemsDto)
                    startActivity(
                        Intent(this@CreateOrderActivity, MainActivity::class.java)
                    )
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(
                        this@CreateOrderActivity,
                        "Ошибка создания заказа: ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                    binding.btnConfirmOrder.isEnabled = true
                } finally {
                    binding.progressCreating.visibility = View.GONE
                }
            }
        }
    }

    private fun updateTotal() {
        val sum = cart.entries.sumOf { it.key.price * it.value }
        binding.tvTotalSum.text = "Итого: $sum сом"
    }

    private fun updateConfirmState() {
        binding.btnConfirmOrder.isEnabled = cart.isNotEmpty()
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        if (item.itemId == android.R.id.home) {
            if (binding.rvProducts.visibility == View.VISIBLE) {
                // вернуться к выбору категории, не закрывая экран
                binding.rvProducts.visibility   = View.GONE
                binding.rvCategories.visibility = View.VISIBLE
            } else {
                finish()
            }
            true
        } else super.onOptionsItemSelected(item)
}
