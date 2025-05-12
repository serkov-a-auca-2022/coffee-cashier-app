package com.example.coffee_cashier_app.network

import java.io.Serializable

/**
 * DTO только для создания заказа — содержит лишь productId и quantity.
 */
data class OrderItemDto(
    val productId: Long,
    val quantity: Int
) : Serializable
