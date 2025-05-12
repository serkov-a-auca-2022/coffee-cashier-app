package com.example.coffee_cashier_app.model

import java.io.Serializable

data class OrderItemResponseDto(
    val productId: Long,
    val name: String,
    val description: String,
    val price: Double,
    val quantity: Int
) : Serializable