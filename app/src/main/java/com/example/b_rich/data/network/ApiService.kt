package com.example.b_rich.data.network

import com.example.b_rich.data.dataModel.CreateTNDWalletRequest
import com.example.b_rich.data.dataModel.ForgotPasswordRequest
import com.example.b_rich.data.dataModel.ForgotPasswordResponse
import com.example.b_rich.data.dataModel.LinkAccountRequest
import com.example.b_rich.data.dataModel.NicknameUpdateRequest
import com.example.b_rich.data.dataModel.PredictionRequest
import com.example.b_rich.data.dataModel.PredictionResponse
import com.example.b_rich.data.dataModel.VerifyCodeResponse
import com.example.b_rich.data.entities.AddAccount
import com.example.b_rich.data.entities.CustomAccount
import com.example.b_rich.data.entities.ExchangeRate
import com.example.b_rich.data.entities.NewsItem
import com.example.b_rich.data.entities.Wallet
import com.example.b_rich.data.entities.user
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
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
    val user: user
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
data class RequestResetResponse(
    val message: String
)

interface ApiService {

    @POST("auth/signup")
    suspend fun createUser(@Body user: user): Response<user>

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("auth/loginwithbiometric")
    suspend fun loginwithbiometric(@Body loginRequest: LoginRequest):Response<LoginResponse>

    @POST("auth/request")
    suspend fun requestReset(@Body body: RequestResetBody): Response<RequestResetResponse>

    @POST("auth/verify")
    suspend fun verifyCode(@Body body: VerifyCodeBody): Response<VerifyCodeResponse>

    @POST("auth/reset")
    suspend fun resetPassword(@Body body: ResetPasswordBody): Response<ResponseReset>

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<ForgotPasswordResponse>





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

    @GET("solana/wallets-with-transactions")
    suspend fun getWalletsWithTransactions(): List<Wallet>

    //Partie Gestion account
    @POST("accounts")
    suspend fun addAccountToUserList(@Body account: AddAccount): Response<CustomAccount>



    // Get all accounts
    @GET("accounts")
    suspend fun getAllAccounts(): Response<List<CustomAccount>>

    // Get account by ID
    @GET("accounts/{id}")
    suspend fun getAccountById(@Path("id") id: String): Response<CustomAccount>

    //Link account
    @POST("accounts/link")
    suspend fun linkAccount(@Body request: LinkAccountRequest): Response<CustomAccount>

    // Delete account by ID
    @DELETE("accounts/{id}")
    suspend fun deleteAccount(@Path("id") id: String): Response<Unit>

    // Get account by RIB
    @GET("accounts/rib/{rib}")
    suspend fun getAccountByRIB(@Path("rib") rib: String): Response<CustomAccount>

    // Update account nickname
    @PATCH("accounts/nickname/{rib}")
    suspend fun updateNickname(
        @Path("rib") rib: String,
        @Body request: NicknameUpdateRequest
    ): Response<CustomAccount>
    // Set default account
    @PATCH("accounts/default/{rib}")
    suspend fun setDefaultAccount(@Path("rib") rib: String): Response<CustomAccount>

    // Get default account
    @GET("accounts/default")
    suspend fun getDefaultAccount(): Response<CustomAccount>

    suspend fun topUpWallet(accountId: String, amount: Double): Response<Unit>
    // Update account balance
    // Get dashboard metrics
    @GET("accounts/dashboard/metrics")
    suspend fun getDashboardMetrics(): Response<Any> // Replace Any with proper metrics type

    // Get account details
    @GET("accounts/{id}/details")
    suspend fun getAccountDetails(@Path("id") id: String): Response<Any> // Replace Any with proper details type

    @POST("solana/create-tnd-wallet")
    suspend fun createTNDWallet(@Body request: CreateTNDWalletRequest): Response<Wallet>
}