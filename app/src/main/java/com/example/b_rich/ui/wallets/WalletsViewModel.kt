package com.example.b_rich.ui.wallets

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.b_rich.data.entities.Transaction
import com.example.b_rich.data.entities.Wallet
import com.example.b_rich.data.network.ApiService
import com.example.b_rich.data.network.RetrofitClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class WalletsViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    // StateFlows for wallets, transactions, and total balance
    private val _wallets = MutableStateFlow<List<Wallet>>(emptyList())
    val wallets = _wallets.asStateFlow()

    private val _recentTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val recentTransactions = _recentTransactions.asStateFlow()

    private val _totalBalance = MutableStateFlow(0.0)
    val totalBalance = _totalBalance.asStateFlow()

    init {
        fetchWallets()
    }

    // Function to fetch wallets
    fun fetchWallets() {
        viewModelScope.launch {
            try {
                val response = apiService.getWallets()
                if (response.isSuccessful) {
                    _wallets.value = response.body() ?: emptyList()
                } else {
                    Log.e("WalletsViewModel", "Error: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("WalletsViewModel", "Error fetching wallets: ${e.message}")
            }
        }
    }
}
