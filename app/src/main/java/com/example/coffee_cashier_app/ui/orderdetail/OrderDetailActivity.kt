package com.example.coffee_cashier_app.ui.orderdetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.coffee_cashier_app.model.Order
import kotlinx.android.synthetic.main.activity_order_detail.*

class OrderDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val order = intent.getSerializableExtra("order") as? Order
        if (order != null) {
            textDateTime.text = "Заказ от ${order.dateTime}"
            val itemsText = order.items.joinToString("\n") {
                "${it.quantity} x ${it.item.name} - ${it.item.price * it.quantity} сом"
            }
            textItemsSummary.text = itemsText
            textTotal.text = "К оплате: ${order.total} сом"
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
