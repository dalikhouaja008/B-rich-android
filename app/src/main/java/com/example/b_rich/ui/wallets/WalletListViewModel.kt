package com.example.b_rich.ui.wallets

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.b_rich.data.entities.CustomAccount
import com.example.b_rich.data.entities.Transaction
import com.example.b_rich.data.entities.Wallet
import com.example.b_rich.data.network.SendTransactionRequest
import com.example.b_rich.data.repositories.WalletRepository
import com.example.b_rich.ui.wallets.components.CreateWalletState
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
sealed class UIState<out T> {
    object Idle : UIState<Nothing>()
    object Loading : UIState<Nothing>()
    data class Success<T>(val data: T) : UIState<T>()
    data class Error(val message: String) : UIState<Nothing>()
}
class WalletsViewModel(private val repository: WalletRepository) : ViewModel() {

    private val _currencyWallets = MutableStateFlow<List<Wallet>>(emptyList())
    val currencyWallets: StateFlow<List<Wallet>> = _currencyWallets

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

    private val _tndWallet = MutableStateFlow<Wallet?>(null)
    val tndWallet: StateFlow<Wallet?> = _tndWallet

    private val _createWalletState = MutableStateFlow<CreateWalletState>(CreateWalletState.Idle)
    val createWalletState: StateFlow<CreateWalletState> = _createWalletState

    private val _sendTransactionState = MutableStateFlow<SendTransactionState>(SendTransactionState.Idle)
    val sendTransactionState: StateFlow<SendTransactionState> = _sendTransactionState.asStateFlow()

    private val _defaultAccount = MutableStateFlow<UIState<CustomAccount>>(UIState.Idle)
    val defaultAccount = _defaultAccount.asStateFlow()

    fun loadDefaultAccount() {
        viewModelScope.launch {
            _defaultAccount.value = UIState.Loading
            try {
                repository.getDefaultAccount()
                    .fold(
                        onSuccess = { account ->
                            _defaultAccount.value = UIState.Success(account)
                        },
                        onFailure = { error ->
                            _defaultAccount.value = UIState.Error(error.message ?: "Unknown error")
                        }
                    )
            } catch (e: Exception) {
                _defaultAccount.value = UIState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun createTNDWallet(amount: Double, rib: String) {
        viewModelScope.launch {
            _createWalletState.value = CreateWalletState.Loading
            try {
                repository.createTNDWallet(amount, rib).fold(
                    onSuccess = { wallet ->
                        _createWalletState.value = CreateWalletState.Success(wallet)
                        fetchWallets() // Rafraîchir la liste des wallets après création
                    },
                    onFailure = { exception ->
                        _createWalletState.value = CreateWalletState.Error(
                            exception.message ?: "Failed to create wallet"
                        )
                    }
                )
            } catch (e: Exception) {
                _createWalletState.value = CreateWalletState.Error(
                    e.message ?: "Failed to create wallet"
                )
            }
        }
    }

    // Réinitialiser l'état lors de la fermeture du dialog
    fun resetCreateWalletState() {
        _createWalletState.value = CreateWalletState.Idle
    }

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
                val fetchedWallets = repository.getWalletsWithTransactions()
                println(fetchedWallets)
                _tndWallet.value = fetchedWallets.find { it.currency == "TND" }
                _currencyWallets.value = fetchedWallets.filter { it.currency != "TND" }
                _hasResponse.value = true
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}