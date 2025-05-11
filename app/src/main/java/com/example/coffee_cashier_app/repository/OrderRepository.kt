package com.example.coffee_cashier_app.repository

import com.example.coffee_cashier_app.model.OrderResponseDto
import com.example.coffee_cashier_app.network.ApiClient
import com.example.coffee_cashier_app.network.CreateOrderRequest
import com.example.coffee_cashier_app.network.OrderItemDto

object OrderRepository {
    private val api = ApiClient.apiService

    suspend fun getActiveOrders(): List<OrderResponseDto> =
        api.getActiveOrders()

    suspend fun getOrderHistory(): List<OrderResponseDto> =
        api.getOrderHistory()

    suspend fun createOrder(items: List<OrderItemDto>): OrderResponseDto {
        val req = CreateOrderRequest(items = items)
        return api.createOrder(req)
    }

    suspend fun finishOrder(orderId: Int): OrderResponseDto =
        api.finishOrder(orderId)

    suspend fun cancelOrder(orderId: Int): OrderResponseDto =
        api.cancelOrder(orderId)

    suspend fun getUserByQr(qrCode: String) = api.getUserByQr(qrCode)
}
