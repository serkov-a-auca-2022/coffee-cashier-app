package com.example.coffee_cashier_app.model

import java.io.Serializable

data class Item(
    val id: Int,
    val name: String,
    val price: Double
) : Serializable
