package com.example.b_rich.data.repositories

import com.example.b_rich.data.entities.Wallet
import com.example.b_rich.data.network.ApiService
import com.example.b_rich.data.network.CurrencyConversionRequest
import com.example.b_rich.data.network.SendTransactionRequest
import retrofit2.Response

class WalletRepository(private val apiService: ApiService) {
    suspend fun getUserWallets(): List<Wallet> = apiService.getUserWallets()

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
}
