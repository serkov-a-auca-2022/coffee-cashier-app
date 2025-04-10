package com.example.coffee_cashier_app.ui.createorder

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.coffee_cashier_app.data.Repository
import com.example.coffee_cashier_app.model.Item
import com.example.coffee_cashier_app.R
import com.example.coffee_cashier_app.model.Order
import com.example.coffee_cashier_app.model.OrderItem
import com.example.coffee_cashier_app.ui.orderdetail.OrderDetailActivity
import kotlinx.android.synthetic.main.activity_create_order.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CreateOrderActivity : AppCompatActivity() {

    private val newOrderItems = mutableListOf<OrderItem>()
    private var nextItemId = 1  // to assign unique IDs to new items

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_order)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        buttonAddItem.setOnClickListener {
            val name = editItemName.text.toString().trim()
            val priceText = editItemPrice.text.toString().trim()
            val quantityText = editItemQuantity.text.toString().trim()
            if (name.isEmpty() || priceText.isEmpty()) {
                Toast.makeText(this, "Введите название и цену товара", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val price = priceText.toDoubleOrNull()
            val quantity = if (quantityText.isNotEmpty()) quantityText.toIntOrNull() ?: 1 else 1
            if (price == null) {
                Toast.makeText(this, "Неверно указана цена", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Create Item and OrderItem, then add to list
            val item = Item(id = nextItemId++, name = name, price = price)
            val orderItem = OrderItem(item = item, quantity = quantity)
            newOrderItems.add(orderItem)
            // Update items list display
            val currentListText = newOrderItems.joinToString("\n") {
                "${it.quantity} x ${it.item.name} - ${it.item.price * it.quantity} сом"
            }
            textItemsList.text = currentListText
            // Clear input fields
            editItemName.text.clear()
            editItemPrice.text.clear()
            editItemQuantity.text.clear()
        }

        buttonConfirmOrder.setOnClickListener {
            if (newOrderItems.isEmpty()) {
                Toast.makeText(this, "Добавьте хотя бы один товар", Toast.LENGTH_SHORT).show()
            } else {
                // Create Order object and send to server
                val totalSum = newOrderItems.sumOf { it.item.price * it.quantity }
                val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                val order = Order(
                    id = 0,
                    items = ArrayList(newOrderItems),
                    dateTime = dateFormat.format(Date()),
                    total = totalSum
                )
                progressCreating.visibility = View.VISIBLE
                lifecycleScope.launch {
                    try {
                        val createdOrder = Repository.createOrder(order)
                        progressCreating.visibility = View.GONE
                        if (createdOrder != null) {
                            // Open detail screen for the new order
                            val intent = Intent(this@CreateOrderActivity, OrderDetailActivity::class.java)
                            intent.putExtra("order", createdOrder)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this@CreateOrderActivity,
                                "Ошибка создания заказа",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } catch (e: Exception) {
                        progressCreating.visibility = View.GONE
                        Toast.makeText(
                            this@CreateOrderActivity,
                            "Ошибка: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
