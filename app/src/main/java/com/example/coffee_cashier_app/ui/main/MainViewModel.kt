package com.example.coffee_cashier_app.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffee_cashier_app.model.OrderResponseDto
import com.example.coffee_cashier_app.repository.OrderRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    val ordersLiveData   = MutableLiveData<List<OrderResponseDto>>()
    val loadingLiveData  = MutableLiveData<Boolean>()

    fun loadOrders() {
        viewModelScope.launch {
            loadingLiveData.postValue(true)
            try {
                val ordersList = OrderRepository.getActiveOrders()
                ordersLiveData.postValue(ordersList)
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error loading orders", e)
            } finally {
                loadingLiveData.postValue(false)
            }
        }
    }
}
