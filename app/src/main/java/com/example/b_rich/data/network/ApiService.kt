package com.example.b_rich.data.network

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.b_rich.data.dataModel.PredictionRequest
import com.example.b_rich.data.dataModel.PredictionResponse
import com.example.b_rich.data.entities.Account
import com.example.b_rich.data.entities.ExchangeRate
import com.example.b_rich.data.entities.NewsItem
import com.example.b_rich.data.entities.Wallet
import com.example.b_rich.data.entities.user
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


// Request model for login
data class LoginRequest(
    val email: String,
    val password: String
)

// Response model for login (adjust to match your backend response)
data class LoginResponse(
    val accessToken: String,  // JWT token returned from the server
    val refreshToken: String,  // Refresh token returned from the server
    val user: user         // User returned from the server
)

data class ResponseReset(
    val success : Boolean,
    val message : String,
    val user: user
)
data class RequestResetBody(
    val email: String
)

data class VerifyCodeBody(
    val email: String,
    val code: String
)

data class CurrencyConversionRequest(
    val amount: Number,
    val fromCurrency: String
)


data class ResetPasswordBody(
    val email: String,
    val code: String,
    val newPassword: String
)

data class SendTransactionRequest(
    val fromWalletPublicKey: String,
    val toWalletPublicKey: String,
    val amount: Double
)

data class TransactionResponse(
    val signature: String
)

data class AccountBalanceResponse(
    val balance: Double
)

interface ApiService {

    @POST("auth/signup")
    suspend fun createUser(@Body user: user): Response<user>

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("auth/loginwithbiometric")
    suspend fun loginwithbiometric(@Body loginRequest: LoginRequest):Response<LoginResponse>

    @POST("auth/request")
    suspend fun requestReset(@Body body: RequestResetBody): Response<ResponseReset>

    @POST("auth/verify")
    suspend fun verifyCode(@Body body: VerifyCodeBody): Response<ResponseReset>

    @POST("auth/reset")
    suspend fun resetPassword(@Body body: ResetPasswordBody): Response<ResponseReset>

    @GET("exchange-rate")
    suspend fun getExchangeRate():Response<List<ExchangeRate>>

    @GET("exchange-rate/sellingRate/{currency}/{amount}")
    suspend fun getSellingRate(
        @Path("currency") currency: String,
        @Path("amount") amount: String
    ): Double

    @GET("exchange-rate/buyingRate/{currency}/{amount}")
    suspend fun getBuyingRate(
        @Path("currency") currency: String,
        @Path("amount") amount: String
    ): Double

    @POST("prediction/create-prediction")
    suspend fun getPredictions(
        @Body request: PredictionRequest
    ): Response<PredictionResponse>

    @GET("news")
    suspend fun getAllNews(): List<NewsItem>

    @POST("solana/transfer-between-wallets")
    suspend fun transferBetweenWallets(@Body request: SendTransactionRequest): String

    @POST("solana/convert-currency")
    suspend fun convertCurrency(
        @Body conversionRequest: CurrencyConversionRequest
    ): Wallet


    @GET("solana/my-wallets")
    suspend fun getUserWallets(): List<Wallet>

    @POST("solana/create-wallet")
    suspend fun createWallet(@Body wallet: Wallet): Wallet


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: Account)

    @Update
    suspend fun updateAccount(account: Account)

    @Delete
    suspend fun deleteAccount(account: Account)

    @Query("SELECT * FROM accounts WHERE account_id = :accountId")
    suspend fun getAccountById(accountId: String): Account?

    @Query("SELECT * FROM accounts WHERE user_id = :userId AND is_default = 1")
    suspend fun getDefaultAccountForUser(userId: String): Account?

    @Query("SELECT * FROM accounts WHERE user_id = :userId")
    suspend fun getAllAccountsForUser(userId: String): List<Account>

    @Query("SELECT SUM(balance) FROM accounts WHERE user_id = :userId")
    suspend fun getTotalBalanceForUser(userId: String): Double

    @GET("/accounts/default-balance/{userId}")
    fun getDefaultAccountBalance(@Path("userId") userId: String): Call<AccountBalanceResponse>
}
