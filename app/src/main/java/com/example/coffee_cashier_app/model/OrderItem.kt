package com.example.coffee_cashier_app.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderItem(
    val item: Item,
    val quantity: Int
) : Parcelable
