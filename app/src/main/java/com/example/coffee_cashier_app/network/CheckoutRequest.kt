package com.example.coffee_cashier_app.network

data class CheckoutRequest(
    val orderId: Long,
    val useFreeDrinks: Int,
    val usePoints: Int
)
