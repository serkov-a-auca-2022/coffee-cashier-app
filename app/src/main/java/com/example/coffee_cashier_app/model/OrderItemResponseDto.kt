package com.example.coffee_cashier_app.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Точно повторяет JSON-поле items[] из /api/orders:
 * {"productId":6,"name":"Ягодный чай","description":"Ягодный чай","price":160.0,"quantity":1}
 */
@Parcelize
data class OrderItemResponseDto(
    val productId: Int,
    val name: String,
    val description: String?,
    val price: Double,
    val quantity: Int
) : Parcelable
