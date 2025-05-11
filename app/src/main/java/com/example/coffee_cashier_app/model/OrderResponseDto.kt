package com.example.coffee_cashier_app.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Точно повторяет JSON-объект заказа из /api/orders и /api/orders/active:
 * {
 *   "orderId":11,
 *   "userId":null,
 *   "orderDate":"2025-05-11T08:15:15.391883794",
 *   "totalAmount":440.0,
 *   "finalAmount":440.0,
 *   "pointsUsed":0.0,
 *   "pointsEarned":22,
 *   "freeDrinkUsed":false,
 *   "status":"CONFIRMED",
 *   "items":[ ... OrderItemResponseDto ... ]
 * }
 */
@Parcelize
data class OrderResponseDto(
    val orderId: Int,
    val userId: Int?,
    val orderDate: String,
    val totalAmount: Double,
    val finalAmount: Double,
    val pointsUsed: Double,
    val pointsEarned: Int,
    val freeDrinkUsed: Boolean,
    val status: String,
    val items: List<OrderItemResponseDto>
) : Parcelable
