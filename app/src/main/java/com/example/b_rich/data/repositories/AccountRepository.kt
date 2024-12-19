package com.example.b_rich.data.repositories

import com.example.b_rich.data.entities.CustomAccount
import com.example.b_rich.data.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AccountRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun fetchAccounts(): List<CustomAccount> = withContext(Dispatchers.IO) {
        val response = apiService.getAllAccounts() // Changed from getUserAccounts to getAllAccounts
        if (response.isSuccessful) {
            response.body() ?: emptyList()
        } else {
            throw Exception("Failed to fetch accounts: ${response.errorBody()?.string()}")
        }
    }

    suspend fun setDefaultAccount(accountId: String) = withContext(Dispatchers.IO) {
        val response = apiService.setDefaultAccount(accountId)
        if (!response.isSuccessful) {
            throw Exception("Failed to set default account: ${response.errorBody()?.string()}")
        }
    }
}