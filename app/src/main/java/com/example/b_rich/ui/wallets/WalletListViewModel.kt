package com.example.b_rich.ui.wallets

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.b_rich.data.entities.Transaction
import com.example.b_rich.data.entities.Wallet
import com.example.b_rich.data.network.SendTransactionRequest
import com.example.b_rich.data.repositories.WalletRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
sealed class SendTransactionState {
    object Idle : SendTransactionState()
    object Loading : SendTransactionState()
    data class Success(val signature: String?= null) : SendTransactionState()
    data class Error(val message: String) : SendTransactionState()
}
class WalletsViewModel(private val repository: WalletRepository) : ViewModel() {

    private val _wallets = MutableStateFlow<List<Wallet>>(emptyList())
    val wallets: StateFlow<List<Wallet>> get() = _wallets

    //transactions récent
    private val _recentTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val recentTransactions: StateFlow<List<Transaction>> get() = _recentTransactions

    private val _convertedWallet = MutableStateFlow<Wallet?>(null)
    val convertedWallet: StateFlow<Wallet?> = _convertedWallet.asStateFlow()

    private val _conversionError = MutableStateFlow<String?>(null)
    val conversionError: StateFlow<String?> = _conversionError.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _hasResponse = MutableStateFlow(false)
    val hasResponse: StateFlow<Boolean> = _hasResponse

    init {
        loadData()
    }

    private val _sendTransactionState = MutableStateFlow<SendTransactionState>(SendTransactionState.Idle)
    val sendTransactionState: StateFlow<SendTransactionState> = _sendTransactionState.asStateFlow()

    fun sendTransaction(
        fromWalletPublicKey: String,
        toWalletPublicKey: String,
        amount: Double
    ) {
        viewModelScope.launch {
            _sendTransactionState.value = SendTransactionState.Loading

            val result = repository.sendTransaction(
                fromWalletPublicKey,
                toWalletPublicKey,
                amount
            )

            _sendTransactionState.value = when {
                result.isSuccess -> {
                    val signature = result.getOrNull()
                    // Mettre à jour les wallets après une transaction réussie
                    fetchWallets()
                    SendTransactionState.Success(signature)
                }
                else -> SendTransactionState.Error(result.exceptionOrNull()?.message ?: "Transaction failed")
            }
        }
    }

    fun SendTransactionState.showNotification(context: Context) {
        when (this) {
            is SendTransactionState.Success -> {
                val message = signature?.let {
                    "Transaction successful - Signature: $it"
                } ?: "Transaction successful"

                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }

            is SendTransactionState.Error -> {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }
    fun convertCurrency(amount: Double, fromCurrency: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _conversionError.value = null

            try {
                val wallet = repository.convertCurrency(amount, fromCurrency)
                _convertedWallet.value = wallet
                // Mettre à jour la liste complète des wallets après la conversion
                fetchWallets()
            } catch (e: Exception) {
                _conversionError.value = e.message ?: "Erreur de conversion"
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun fetchWallets() {
        viewModelScope.launch {
            try {
                val fetchedWallets = repository.getUserWallets()
                println(fetchedWallets)
                _wallets.value = fetchedWallets
                _hasResponse.value = true
            } catch (e: Exception) {
                // Handle error
            }
        }
    }



    private fun loadData() {
        viewModelScope.launch {
            // Définition des transactions récentes
            _recentTransactions.value = listOf(
                Transaction(1, "Completed", "Deposit", 200.0, Date()),
                Transaction(2, "Completed", "Shopping", -50.0, Date()),
                Transaction(3, "Pending", "Transfer to USD", -100.0, Date())
            )
        }
    }
}