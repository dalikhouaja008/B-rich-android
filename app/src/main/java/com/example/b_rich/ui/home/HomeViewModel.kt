package com.example.b_rich.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.b_rich.data.Transaction.Transaction
import com.example.b_rich.data.Wallet.Wallet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date

class HomeViewModel : ViewModel() {

    // MutableStateFlow to hold the data
    private val _totalBalance = MutableStateFlow(0.0)
    private val _wallets = MutableStateFlow<List<Wallet>>(emptyList())
    private val _recentTransactions = MutableStateFlow<List<Transaction>>(emptyList())

    // Public property to expose StateFlow
    val totalBalance: StateFlow<Double> get() = _totalBalance
    val wallets: StateFlow<List<Wallet>> get() = _wallets
    val recentTransactions: StateFlow<List<Transaction>> get() = _recentTransactions

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            // Simulate loading data
            _totalBalance.value = 4800.00
            _wallets.value = listOf(
                Wallet(currency = "Tunisian Dinar", symbol = "TND", balance = 2500.00, transactions = emptyList()),
                Wallet(currency = "Euro", symbol = "€", balance = 800.00, transactions = emptyList()),
                Wallet(currency = "US Dollar", symbol = "$", balance = 1500.00, transactions = emptyList())
            )
            _recentTransactions.value = listOf(
                Transaction(id = 1, status = "Completed", description = "Deposit", amount = 200.00, date = Date()),
                Transaction(id = 2, status = "Completed", description = "Shopping", amount = -50.00, date = Date())
            )
        }
    }
}