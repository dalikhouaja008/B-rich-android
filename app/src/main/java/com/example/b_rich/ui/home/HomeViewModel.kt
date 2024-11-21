package com.example.b_rich.ui.home

import androidx.lifecycle.ViewModel
import com.example.b_rich.data.Wallet.Wallet
import com.example.b_rich.data.Transaction.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Date

class HomeViewModel : ViewModel() {

    private val _totalBalance = MutableStateFlow(1234.56)
    val totalBalance: StateFlow<Double> get() = _totalBalance

    private val _wallets = MutableStateFlow(
        listOf(
            Wallet(currency = "USD", symbol = "$", balance = 500.0),
            Wallet(currency = "EUR", symbol = "€", balance = 300.0),
            Wallet(currency = "TND", symbol = "د.ت", balance = 2500.0)
        )
    )
    val wallets: StateFlow<List<Wallet>> get() = _wallets

    private val _recentTransactions = MutableStateFlow(
        listOf(
            Transaction(id = 1, status = "Completed", description = "Grocery", amount = -50.0, date = Date()),
            Transaction(id = 2, status = "Completed", description = "Salary", amount = 2000.0, date = Date()),
            Transaction(id = 3, status = "Completed", description = "Coffee", amount = -5.0, date = Date())
        )
    )
    val recentTransactions: StateFlow<List<Transaction>> get() = _recentTransactions


    fun addWallet(wallet: Wallet) {
        _wallets.value = _wallets.value + wallet
    }


    fun addTransaction(transaction: Transaction) {
        _recentTransactions.value = _recentTransactions.value + transaction
    }


    fun updateTotalBalance(newBalance: Double) {
        _totalBalance.value = newBalance
    }
}