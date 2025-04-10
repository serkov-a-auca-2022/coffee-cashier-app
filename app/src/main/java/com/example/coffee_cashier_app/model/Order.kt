package com.example.coffee_cashier_app.model

import java.io.Serializable

data class Order(
    val id: Int,
    val items: List<OrderItem>,
    val dateTime: String,
    val total: Double
) : Serializable
