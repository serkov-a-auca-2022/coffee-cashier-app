package com.example.coffee_cashier_app.model


data class UserResponseDto(
    val id: Long,
    val firstName: String?,
    val lastName: String?,
    val points: Double,
    val freeDrinks: Int
)
