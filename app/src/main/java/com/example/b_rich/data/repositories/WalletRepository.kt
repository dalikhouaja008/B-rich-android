package com.example.b_rich.data.repositories

import com.example.b_rich.data.dataModel.CreateTNDWalletRequest
import com.example.b_rich.data.entities.CustomAccount
import com.example.b_rich.data.entities.Wallet
import com.example.b_rich.data.network.ApiService
import com.example.b_rich.data.network.CurrencyConversionRequest
import com.example.b_rich.data.network.SendTransactionRequest
import retrofit2.Response

class WalletRepository(private val apiService: ApiService) {
    suspend fun getUserWallets(): List<Wallet> = apiService.getUserWallets()

    suspend fun getWalletsWithTransactions(): List<Wallet> {
        return apiService.getWalletsWithTransactions()
    }
    suspend fun getDefaultAccount(): Result<CustomAccount> = try {
        val response = apiService.getDefaultAccount()
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }
    suspend fun sendTransaction(
        fromWalletPublicKey: String,
        toWalletPublicKey: String,
        amount: Double
    ): Result<String> {
        return try {
            val request = SendTransactionRequest(
                fromWalletPublicKey = fromWalletPublicKey,
                toWalletPublicKey = toWalletPublicKey,
                amount = amount
            )
            val signature = apiService.transferBetweenWallets(request)
            Result.success(signature)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun convertCurrency(amount: Double, fromCurrency: String): Wallet {
        return apiService.convertCurrency(CurrencyConversionRequest(amount, fromCurrency))
    }

    suspend fun createTNDWallet(amount: Double): Result<Wallet> {
        return try {
            val request = CreateTNDWalletRequest(amount)
            val response = apiService.createTNDWallet(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
