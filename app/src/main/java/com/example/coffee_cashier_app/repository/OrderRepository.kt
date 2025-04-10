package com.example.coffee_cashier_app.repository

import com.example.coffee_cashier_app.model.*
import com.example.coffee_cashier_app.network.*

class OrderRepository {
    private val api = ApiClient.apiService  // экземпляр API Service

    // Получить список активных заказов (с сервера)
    suspend fun getActiveOrders(): List<Order> {
        return api.getActiveOrders()
    }

    // Получить историю заказов (все завершённые и отменённые)
    suspend fun getOrderHistory(): List<Order> {
        return api.getOrderHistory()
    }

    // Создать новый заказ
    suspend fun createOrder(items: List<OrderItem>): Order {
        val request = CreateOrderRequest(items)
        return api.createOrder(request)
    }

    // Завершить заказ (назначить пользователя и применить бонусы)
    suspend fun completeOrder(orderId: Int, userId: Int, pointsUsed: Int, freeDrinksUsed: Int): Order {
        val request = CompleteOrderRequest(userId, pointsUsed, freeDrinksUsed)
        return api.completeOrder(orderId, request)
    }

    // Отменить заказ
    suspend fun cancelOrder(orderId: Int): Order {
        return api.cancelOrder(orderId)
    }

    // Найти пользователя по QR-коду
    suspend fun getUserByQr(qrCode: String): User {
        return api.getUserByQr(qrCode)
    }
}
