package com.example.b_rich.data.repositories

import android.util.Log
import com.example.b_rich.data.entities.Account
import com.example.b_rich.data.network.ApiService


class AccountRepository(private val apiService: ApiService) {

    suspend fun insertAccount(account: Account) {
        apiService.insertAccount(account)
    }

    suspend fun updateAccount(account: Account) {
        apiService.updateAccount(account)
    }

    suspend fun deleteAccount(account: Account) {
        apiService.deleteAccount(account)
    }

    suspend fun getAccountById(accountId: String): Account? {
        return apiService.getAccountById(accountId)
    }

    suspend fun getDefaultAccountForUser(userId: String): Account? {
        return apiService.getDefaultAccountForUser(userId)
    }

    suspend fun getAllAccountsForUser(userId: String): List<Account> {
        return apiService.getAllAccountsForUser(userId)
    }

    suspend fun getTotalBalanceForUser(userId: String): Double {
        return try {
            apiService.getTotalBalanceForUser(userId)
        } catch (e: Exception) {
            Log.e("AccountRepository", "Error fetching total balance", e)
            0.0
        }
    }
}
