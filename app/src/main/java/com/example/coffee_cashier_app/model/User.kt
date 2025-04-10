package com.example.coffee_cashier_app.model

import java.io.Serializable

data class User(
    val id: Int,
    val name: String,
    val email: String
) : Serializable
