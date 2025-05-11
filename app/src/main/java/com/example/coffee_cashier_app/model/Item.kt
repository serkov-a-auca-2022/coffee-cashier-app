package com.example.coffee_cashier_app.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Продукт. Поле `category` — та самая текстовая метка,
 * по которой мы группируем.
 */
@Parcelize
data class Item(
    val id: Int,
    val name: String,
    val price: Double,
    val category: String
) : Parcelable
