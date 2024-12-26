package com.example.b_rich.ui.listAccounts

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.b_rich.data.entities.CustomAccount
import com.example.b_rich.data.network.ApiService
import com.example.b_rich.data.repositories.AccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class AccountsUiState {
    object Loading : AccountsUiState()
    data class Success(val accounts: List<CustomAccount>) : AccountsUiState()
    data class Error(val message: String) : AccountsUiState()
}

sealed class TopUpResult {
    object Success : TopUpResult()
    data class Error(val message: String) : TopUpResult()
}

class ListAccountsViewModel(
    private val apiService: ApiService,
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AccountsUiState>(AccountsUiState.Loading)
    val uiState: StateFlow<AccountsUiState> = _uiState

    private val _accounts = MutableStateFlow<List<CustomAccount>>(emptyList())
    val accounts: StateFlow<List<CustomAccount>> = _accounts

    private val _selectedAccount = MutableStateFlow<CustomAccount?>(null)
    val selectedAccount: StateFlow<CustomAccount?> = _selectedAccount

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isTopUpInProgress = MutableStateFlow(false)
    val isTopUpInProgress: StateFlow<Boolean> = _isTopUpInProgress

    fun refreshAccounts() {
        viewModelScope.launch {
            _uiState.value = AccountsUiState.Loading
            _isLoading.value = true
            Log.d("ListAccountsViewModel", "Starting to fetch accounts")

            try {
                val accountsList = accountRepository.fetchAccounts()
                _accounts.value = accountsList
                _uiState.value = AccountsUiState.Success(accountsList)
                Log.d("ListAccountsViewModel", "Accounts fetched successfully: $accountsList")

                // If we have a selected account, update its data
                _selectedAccount.value?.let { selected ->
                    _selectedAccount.value = accountsList.find { it.id == selected.id }
                }
            } catch (e: Exception) {
                _uiState.value = AccountsUiState.Error(e.message ?: "Unknown error occurred")
                Log.e("ListAccountsViewModel", "Error fetching accounts", e)
            } finally {
                _isLoading.value = false
                Log.d("ListAccountsViewModel", "Loading state: ${_isLoading.value}")
            }
        }
    }

    fun selectAccount(account: CustomAccount) {
        _selectedAccount.value = account
    }

    fun toggleDefault(account: CustomAccount) {
        viewModelScope.launch {
            try {
                accountRepository.setDefaultAccount(account.rib)
                // Update the accounts list with the new default status
                _accounts.update { currentAccounts ->
                    currentAccounts.map { existingAccount ->
                        when {
                            existingAccount.id == account.id -> existingAccount.copy(isDefault = true)
                            existingAccount.isDefault == true -> existingAccount.copy(isDefault = false)
                            else -> existingAccount
                        }
                    }
                }

                // Update selected account if it was affected
                _selectedAccount.value?.let { selected ->
                    _selectedAccount.value = _accounts.value.find { it.id == selected.id }
                }
            } catch (e: Exception) {
                // Handle error
                Log.e("ListAccountsViewModel", "Error setting default account", e)
            }
        }
    }

    suspend fun topUpWallet(accountId: String, amount: Double): TopUpResult {
        return try {
            _isTopUpInProgress.value = true
            val response = apiService.topUpWallet(accountId, amount)

            if (response.isSuccessful) {
                refreshAccounts() // Refresh to get updated balance
                TopUpResult.Success
            } else {
                TopUpResult.Error("Failed to top up wallet")
            }
        } catch (e: Exception) {
            TopUpResult.Error(e.message ?: "Unknown error occurred")
        } finally {
            _isTopUpInProgress.value = false
        }
    }

    fun clearSelectedAccount() {
        _selectedAccount.value = null
    }
}