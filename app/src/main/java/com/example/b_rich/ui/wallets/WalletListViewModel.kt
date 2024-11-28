package com.example.b_rich.ui.wallets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.b_rich.data.entities.Transaction
import com.example.b_rich.data.entities.Wallet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class WalletsViewModel : ViewModel() {

    private val _wallets = MutableStateFlow<List<Wallet>>(emptyList())
    val wallets: StateFlow<List<Wallet>> get() = _wallets

    //transactions récent
    private val _recentTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val recentTransactions: StateFlow<List<Transaction>> get() = _recentTransactions

    //solde total
    private val _totalBalance = MutableStateFlow(0.0)
    val totalBalance: StateFlow<Double> get() = _totalBalance

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val walletsList = listOf(
                Wallet(
                    currency = "Tunisian Dinar",
                    symbol = "TND",
                    balance = 1500.0,
                    transactions = emptyList()
                ),
                Wallet(
                    currency = "Euro",
                    symbol = "€",
                    balance = 500.0,
                    transactions = emptyList()
                ),
                Wallet(
                    currency = "US Dollar",
                    symbol = "$",
                    balance = 800.0,
                    transactions = emptyList()
                )
            )
            _wallets.value = walletsList

            // Définition des transactions récentes
            _recentTransactions.value = listOf(
                Transaction(1, "Completed", "Deposit", 200.0, Date()),
                Transaction(2, "Completed", "Shopping", -50.0, Date()),
                Transaction(3, "Pending", "Transfer to USD", -100.0, Date())
            )

            // Calcul du solde total
            _totalBalance.value = walletsList.sumOf { it.balance }
        }
    }
}