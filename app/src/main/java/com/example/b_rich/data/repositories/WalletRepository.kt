package com.example.b_rich.data.repositories

import com.example.b_rich.data.entities.Transaction
import com.example.b_rich.data.entities.Wallet
import com.example.b_rich.data.network.ApiService
import retrofit2.Response
import javax.inject.Inject

/*
class WalletRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getWallets(): Response<List<Wallet>> {
        return apiService.getWallets()
    }

    suspend fun getWalletTransactions(walletId: String): Response<List<Transaction>> {
        return apiService.getTransactions(walletId)
    }

    suspend fun getRecentTransactions(): Response<List<Transaction>> {
        return apiService.getRecentTransactions()
    }
}
 */

class WalletRepository(private val apiService: ApiService) {

    suspend fun fetchWallets(): Response<List<Wallet>> {
        return apiService.getWallets()
    }

    suspend fun fetchRecentTransactions(): Response<List<Transaction>> {
        return apiService.getRecentTransactions()
    }

    suspend fun fetchWalletTransactions(walletId: String): Response<List<Transaction>> {
        return apiService.getTransactions(walletId)
    }
}
