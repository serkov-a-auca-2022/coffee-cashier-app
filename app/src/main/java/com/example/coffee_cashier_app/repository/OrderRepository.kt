package com.example.coffee_cashier_app.repository

import com.example.coffee_cashier_app.model.OrderResponseDto
import com.example.coffee_cashier_app.model.UserResponseDto
import com.example.coffee_cashier_app.network.ApiClient
import com.example.coffee_cashier_app.network.CheckoutRequest
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

    /**  Теперь возвращаем ваш DTO  */
    suspend fun getUserByQr(qrCode: String): UserResponseDto =
        api.getUserByQr(qrCode)

    suspend fun assignUser(orderId: Int, userId: Long): OrderResponseDto =
        api.assignUser(orderId, userId)

    suspend fun checkoutWithRewards(
        orderId: Int,
        useFreeDrinks: Int,
        usePoints: Int
    ): OrderResponseDto =
        api.checkout(CheckoutRequest(orderId.toLong(), useFreeDrinks, usePoints))

}
