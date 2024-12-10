package com.example.b_rich.data.repositories

import com.example.b_rich.data.entities.Wallet
import com.example.b_rich.ui.wallets.components.WalletCreationBottomSheet
import com.example.b_rich.data.network.ApiService
import com.example.b_rich.data.network.CurrencyConversionRequest
import com.example.b_rich.data.network.SendTransactionRequest
import retrofit2.Response
import java.util.Date
import java.util.UUID

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

    suspend fun createFirstWallet(currency: String, initialBalance: Double): Wallet {
        return try {
            // Create a new wallet object
            val newWallet = Wallet(
                id = UUID.randomUUID().toString(),
                userId = "currentUserId", // Replace with actual user ID logic
                publicKey = "generatedPublicKey", // Replace with actual key generation logic
                privateKey = "generatedPrivateKey", // Replace with actual key generation logic
                type = "default", // Specify wallet type if necessary
                network = "defaultNetwork", // Specify network if necessary
                balance = initialBalance,
                createdAt = Date(),
                currency = currency,
                originalAmount = initialBalance,
                convertedAmount = initialBalance // Adjust if conversion is needed
            )

            // Call the API to save the wallet (assuming you have an endpoint for this)
            apiService.createWallet(newWallet)

            // Return the newly created wallet
            newWallet
        } catch (e: Exception) {
            // Handle any errors that occur during wallet creation
            throw e
        }
    }
}
