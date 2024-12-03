package com.example.b_rich.ui.wallets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.b_rich.data.entities.Transaction
import com.example.b_rich.data.entities.Wallet
import com.example.b_rich.data.repositories.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletsViewModel @Inject constructor(
    private val walletsRepository: WalletRepository
) : ViewModel() {

    private val _wallets = MutableStateFlow<List<Wallet>>(emptyList())
    val wallets: StateFlow<List<Wallet>> = _wallets

    private val _recentTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val recentTransactions: StateFlow<List<Transaction>> = _recentTransactions

    private val _totalBalance = MutableStateFlow<Double>(0.0)
    val totalBalance: StateFlow<Double> = _totalBalance

    init {
        fetchWallets()  // Appel à fetchWallets() du Repository
        fetchRecentTransactions()  // Appel à fetchRecentTransactions() du Repository
    }

    // Méthodes publiques pour récupérer les données
    fun fetchWallets() {
        viewModelScope.launch {
            try {
                val response = walletsRepository.fetchWallets()  // Utiliser fetchWallets ici
                if (response.isSuccessful) {
                    _wallets.value = response.body() ?: emptyList()
                    calculateTotalBalance()
                } else {
                    // Gérer l'échec de l'API
                }
            } catch (e: Exception) {
                // Gérer les erreurs réseau ou API
            }
        }
    }

    fun fetchRecentTransactions() {
        viewModelScope.launch {
            try {
                val response = walletsRepository.fetchRecentTransactions()  // Utiliser fetchRecentTransactions ici
                if (response.isSuccessful) {
                    _recentTransactions.value = response.body() ?: emptyList()
                } else {
                    // Gérer l'échec de l'API
                }
            } catch (e: Exception) {
                // Gérer les erreurs réseau ou API
            }
        }
    }

    private fun calculateTotalBalance() {
        _totalBalance.value = _wallets.value.sumOf { it.balance }
    }
}
