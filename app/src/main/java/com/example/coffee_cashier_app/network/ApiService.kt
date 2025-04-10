package com.example.coffee_cashier_app.network


import com.example.coffee_cashier_app.model.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.Call  // если используем Call для асинхронности
import kotlinx.coroutines.Deferred  // можно использовать Coroutines

// DTO для создания нового заказа
data class CreateOrderRequest(
    val items: List<OrderItem>  // список позиций (товар + количество)
)

// DTO для завершения заказа
data class CompleteOrderRequest(
    val userId: Int,
    val pointsUsed: Int,
    val freeDrinksUsed: Int
)

// Интерфейс API для запросов к серверу
interface ApiService {

    @GET("orders/active")
    suspend fun getActiveOrders(): List<Order>

    @GET("orders/history")
    suspend fun getOrderHistory(): List<Order>

    @POST("orders")
    suspend fun createOrder(@Body order: CreateOrderRequest): Order

    @PUT("orders/{id}/complete")
    suspend fun completeOrder(
        @Path("id") orderId: Int,
        @Body completeRequest: CompleteOrderRequest
    ): Order

    @PUT("orders/{id}/cancel")
    suspend fun cancelOrder(@Path("id") orderId: Int): Order

    @GET("users/qr/{qrCode}")
    suspend fun getUserByQr(@Path("qrCode") qrCode: String): User
}