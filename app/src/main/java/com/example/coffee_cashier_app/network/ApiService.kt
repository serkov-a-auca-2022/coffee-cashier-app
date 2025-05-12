package com.example.coffee_cashier_app.network


import com.example.coffee_cashier_app.model.Item
import com.example.coffee_cashier_app.model.OrderResponseDto
import com.example.coffee_cashier_app.model.UserResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class LoginRequest(val username: String, val password: String)
data class Cashier(val id: Long, val username: String, val fullName: String)

// DTO для создания заказа

data class CreateOrderRequest(
    val items: List<OrderItemDto>,
    val userId: Int? = null,
    val userQrCode: String? = null,
    val pointsToUse: Int? = null,
    val useFreeDrink: Boolean? = null,
    val status: String? = null
)
// Теперь «финиш» и «отмена» без тела (POST)
interface ApiService {
    // кассир
    @POST("cashiers/login")
    suspend fun login(@Body request: LoginRequest): Response<Cashier>

    // товары
    @GET("products")
    suspend fun getProducts(): List<Item>

    // активные заказы (CONFIRMED)
    @GET("orders/active")
    suspend fun getActiveOrders(): List<OrderResponseDto>

    // история по кассиру / для всех завершённых + отменённых
    // пока нет серверного эндпоинта — оставляю тот, что был
    @GET("orders/history")
    suspend fun getOrderHistory(): List<OrderResponseDto>

    // создать или обновить (по новой логике бэка)
    @POST("orders")
    suspend fun createOrder(@Body request: CreateOrderRequest): OrderResponseDto

    // завершить заказ (PUT /orders/{id}/finish)
    @PUT("orders/{id}/finish")
    suspend fun finishOrder(@Path("id") orderId: Int): OrderResponseDto

    // отменить заказ (PUT /orders/{id}/cancel)
    @PUT("orders/{id}/cancel")
    suspend fun cancelOrder(@Path("id") orderId: Int): OrderResponseDto

    // привязать клиента по QR
    @GET("users/qr/{qrCode}")
    suspend fun getUserByQr(@Path("qrCode") qr: String): UserResponseDto
}
