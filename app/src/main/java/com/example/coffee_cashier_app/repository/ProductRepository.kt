package com.example.coffee_cashier_app.repository

import com.example.coffee_cashier_app.model.Item
import com.example.coffee_cashier_app.network.ApiClient

/**
 * Всё, что связано с товарами.
 */
object ProductRepository {
    private val api = ApiClient.apiService

    suspend fun getAll(): List<Item> =
        api.getProducts()
}
