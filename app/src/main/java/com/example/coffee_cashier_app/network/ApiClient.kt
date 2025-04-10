package com.example.coffee_cashier_app.network

import com.example.coffee_cashier_app.model.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.Call  // если используем Call для асинхронности
import kotlinx.coroutines.Deferred  // можно использовать Coroutines

// Объект-одиночка для настройки Retrofit
object ApiClient {
    private const val BASE_URL = "https://10.0.2.2:8080/"  // базовый URL REST API

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}