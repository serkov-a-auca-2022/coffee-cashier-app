package com.example.coffee_cashier_app.model

import java.io.Serializable

data class OrderResponseDto(
    val orderId: Long,
    val userId: Long?,
    val orderDate: String,
    val totalAmount: Double,
    val finalAmount: Double,
    val pointsUsed: Double,
    val pointsEarned: Int,
    val freeDrinkUsed: Boolean,
    val status: String,
    val items: List<OrderItemResponseDto>,
    val user: UserResponseDto?,
    val freeDrinksUsedCount: Int
) : Serializable