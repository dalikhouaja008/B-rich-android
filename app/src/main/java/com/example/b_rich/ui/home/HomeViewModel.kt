package com.example.b_rich.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.b_rich.data.Wallet.Wallet
import com.example.b_rich.data.Transaction.Transaction
import kotlinx.coroutines.launch
import java.util.Date

class HomeViewModel : ViewModel() {

    private var _totalBalance = 1234.56
    val totalBalance: Double
        get() = _totalBalance

    private val _wallets = mutableListOf(
        Wallet(currency = "USD", symbol = "$", balance = 500.0),
        Wallet(currency = "EUR", symbol = "€", balance = 300.0),
        Wallet(currency = "BTC", symbol = "₿", balance = 0.1)
    )
    val wallets: List<Wallet> get() = _wallets

    private val _recentTransactions = mutableListOf(
        Transaction(id = 1, status = "Completed", description = "Grocery", amount = -50.0, date = Date()),
        Transaction(id = 2, status = "Completed", description = "Salary", amount = 2000.0, date = Date()),
        Transaction(id = 3, status = "Completed", description = "Coffee", amount = -5.0, date = Date())
    )
    val recentTransactions: List<Transaction> get() = _recentTransactions

    fun addWallet(wallet: Wallet) {
        viewModelScope.launch {
            _wallets.add(wallet)
        }
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            _recentTransactions.add(transaction)
        }
    }

    fun updateTotalBalance(newBalance: Double) {
        viewModelScope.launch {
            _totalBalance = newBalance
        }
    }
}
