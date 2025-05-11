package com.example.coffee_cashier_app.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    val id: Int,
    val items: List<OrderItem>,
    val dateTime: String,
    val total: Double
) : Parcelable
