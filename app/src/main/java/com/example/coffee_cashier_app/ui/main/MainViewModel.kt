package com.example.coffee_cashier_app.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffee_cashier_app.data.Repository
import com.example.coffee_cashier_app.model.Order
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    val ordersLiveData: MutableLiveData<List<Order>> = MutableLiveData()
    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun loadOrders() {
        viewModelScope.launch {
            loadingLiveData.postValue(true)
            try {
                val ordersList = Repository.getOrders()
                ordersLiveData.postValue(ordersList)
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error loading orders", e)
            } finally {
                loadingLiveData.postValue(false)
            }
        }
    }
}
