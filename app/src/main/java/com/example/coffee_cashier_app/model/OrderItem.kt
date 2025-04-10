package com.example.coffee_cashier_app.model

import java.io.Serializable

data class OrderItem(
    val item: Item,
    val quantity: Int
) : Serializable
