package com.example.coffee_cashier_app.data

import com.example.coffee_cashier_app.model.OrderResponseDto
import com.example.coffee_cashier_app.network.ApiClient
import com.example.coffee_cashier_app.network.Cashier
import com.example.coffee_cashier_app.network.LoginRequest
import retrofit2.Response

object Repository {
    private val api = ApiClient.apiService

    // Логин — только по username/password, регистрации нет
    suspend fun login(username: String, password: String): Response<Cashier> {
        return api.login(LoginRequest(username, password))
    }

    // Список активных заказов для главного экрана
    suspend fun getActiveOrders(): List<OrderResponseDto> = api.getActiveOrders()
}
