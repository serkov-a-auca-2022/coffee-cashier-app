package com.example.coffee_cashier_app.model

// Перечисление статусов заказа

enum class OrderStatus {
    ACTIVE,     // Активный (не завершён, не отменён)
    COMPLETED,  // Завершён
    CANCELLED   // Отменён
}