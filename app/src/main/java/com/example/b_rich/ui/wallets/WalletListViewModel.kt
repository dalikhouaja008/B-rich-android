package com.example.b_rich.ui.wallets

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.b_rich.data.entities.Transaction
import com.example.b_rich.data.entities.Wallet
import com.example.b_rich.data.repositories.WalletRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

sealed class SendTransactionState {
    object Idle : SendTransactionState()
    object Loading : SendTransactionState()
    data class Success(val signature: String? = null) : SendTransactionState()
    data class Error(val message: String) : SendTransactionState()
}

class WalletsViewModel(
    private val repository: WalletRepository,
    private val context: Context
) : ViewModel() {

    private val _wallets = MutableStateFlow<List<Wallet>>(emptyList())
    val wallets: StateFlow<List<Wallet>> = _wallets

    private val _recentTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val recentTransactions: StateFlow<List<Transaction>> = _recentTransactions

    private val _sendTransactionState = MutableStateFlow<SendTransactionState>(SendTransactionState.Idle)
    val sendTransactionState: StateFlow<SendTransactionState> = _sendTransactionState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _hasResponse = MutableStateFlow(false)
    val hasResponse: StateFlow<Boolean> = _hasResponse

    private val _conversionError = MutableStateFlow<String?>(null)
    val conversionError: StateFlow<String?> = _conversionError

    private val _convertedWallet = MutableStateFlow<Wallet?>(null)
    val convertedWallet: StateFlow<Wallet?> = _convertedWallet

    init {
        fetchWallets()
    }

    fun sendTransaction(fromWalletPublicKey: String, toWalletPublicKey: String, amount: Double) {
        viewModelScope.launch {
            _sendTransactionState.value = SendTransactionState.Loading
            val result = repository.sendTransaction(fromWalletPublicKey, toWalletPublicKey, amount)

            _sendTransactionState.value = if (result.isSuccess) {
                fetchWallets()
                SendTransactionState.Success(result.getOrNull())
            } else {
                SendTransactionState.Error(result.exceptionOrNull()?.message ?: "Transaction failed")
            }
        }
    }

    fun SendTransactionState.showNotification(context: Context) {
        when (this) {
            is SendTransactionState.Success -> {
                Toast.makeText(context, "Transaction successful", Toast.LENGTH_SHORT).show()
            }
            is SendTransactionState.Error -> {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }

    fun fetchWallets() {
        viewModelScope.launch {
            try {
                _wallets.value = repository.getUserWallets()
                _hasResponse.value = true
            } catch (e: Exception) {
                _hasResponse.value = true
                // Log error or handle it appropriately
                Toast.makeText(
                    context,
                    "Failed to fetch wallets: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun addWallet(wallet: Wallet) {
        viewModelScope.launch {
            try {
                // Generate a unique ID for the wallet
                val newWallet = wallet.copy(
                    id = UUID.randomUUID().toString(),
                    userId = getCurrentUserId(),
                    createdAt = Date() // Set current timestamp
                )
                repository.addWallet(newWallet)
                fetchWallets() // Refresh wallet list
            } catch (e: Exception) {
                // Handle error
                Toast.makeText(
                    context,
                    "Failed to create wallet: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun convertCurrency(amount: Double, fromCurrency: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _conversionError.value = null

                val convertedWallet = convertCurrencyLogic(amount, fromCurrency)

                _convertedWallet.value = convertedWallet
                _isLoading.value = false
            } catch (e: Exception) {
                _conversionError.value = e.message ?: "Currency conversion failed"
                _isLoading.value = false
                Toast.makeText(
                    context,
                    "Conversion error: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // Implement method to get current user's ID
    private fun getCurrentUserId(): String {
        // This should be replaced with actual user authentication logic
        return "current_user_id"
    }

    // Placeholder conversion method - replace with actual implementation
    private fun performCurrencyConversion(
        amount: Double,
        fromCurrency: String,
        toCurrency: String
    ): Double {
        // This is a stub - implement actual currency conversion logic
        // You might want to use an exchange rate API or service
        return when {
            fromCurrency == toCurrency -> amount
            fromCurrency == "TND" && toCurrency == "USD" -> amount * 0.3
            fromCurrency == "USD" && toCurrency == "TND" -> amount / 0.3
            else -> amount // Default case
        }
    }

    private fun convertCurrencyLogic(amount: Double, fromCurrency: String): Wallet {
        // Currency conversion logic (replace with actual exchange rate service)
        val convertedAmount = when (fromCurrency) {
            "TND" -> amount * 0.3 // Example conversion rate
            "USD" -> amount
            else -> amount // Default case if currency not specified
        }

        return Wallet(
            userId = getCurrentUserId(),
            publicKey = generatePublicKey(), // Method to generate public key
            privateKey = generatePrivateKey(), // Method to generate private key
            type = "converted", // Specify wallet type
            network = "Solana", // Specify network
            balance = convertedAmount,
            currency = when (fromCurrency) {
                "TND" -> "USD"
                else -> fromCurrency
            },
            originalAmount = amount,
            convertedAmount = convertedAmount
        )
    }

    // Utility methods for generating keys (these should be implemented securely)
    private fun generatePublicKey(): String {
        // Implement secure public key generation
        return UUID.randomUUID().toString()
    }

    private fun generatePrivateKey(): String {
        // Implement secure private key generation
        return UUID.randomUUID().toString()
    }
}