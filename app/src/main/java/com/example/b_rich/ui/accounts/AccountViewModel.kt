package com.example.b_rich.ui.accounts

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.b_rich.data.entities.Account
import com.example.b_rich.data.repositories.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _totalBalance = MutableStateFlow<Double>(0.0)
    val totalBalance: StateFlow<Double> = _totalBalance

    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts: StateFlow<List<Account>> = _accounts

    init {
        fetchTotalBalance()
    }

    fun addAccount(account: Account) {
        _accounts.value = _accounts.value + account
    }

    fun createAccount(account: Account) {
        viewModelScope.launch {
            accountRepository.insertAccount(account)
            fetchTotalBalance() // Refresh total balance after creating an account
        }
    }

    fun updateAccount(account: Account) {
        viewModelScope.launch {
            accountRepository.updateAccount(account)
            fetchTotalBalance() // Refresh total balance after updating an account
        }
    }

    fun deleteAccount(account: Account) {
        viewModelScope.launch {
            accountRepository.deleteAccount(account)
            fetchTotalBalance() // Refresh total balance after deleting an account
        }
    }

    fun fetchTotalBalance() {
        viewModelScope.launch {
            try {
                val userId = getCurrentUserId() // Implement your user ID retrieval
                _totalBalance.value = accountRepository.getTotalBalanceForUser(userId)
            } catch (e: Exception) {
                Log.e("AccountViewModel", "Error fetching total balance", e)
                _totalBalance.value = 0.0
            }
        }
    }


    // Helper method to get current user ID (replace with your actual authentication logic)
    private fun getCurrentUserId(): String {
        // Implement logic to retrieve current user ID
        return "current_user_id"
    }

    suspend fun getAccountById(accountId: String): Account? {
        return accountRepository.getAccountById(accountId)
    }

    suspend fun getDefaultAccountForUser(userId: String): Account? {
        return accountRepository.getDefaultAccountForUser(userId)
    }

    suspend fun getAllAccountsForUser(userId: String): List<Account> {
        return accountRepository.getAllAccountsForUser(userId)
    }
}