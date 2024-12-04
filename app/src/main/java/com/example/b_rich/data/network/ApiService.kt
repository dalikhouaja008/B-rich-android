package com.example.b_rich.data.network

import android.accounts.Account
import com.example.b_rich.data.dataModel.PredictionRequest
import com.example.b_rich.data.dataModel.PredictionResponse
import com.example.b_rich.data.entities.ExchangeRate
import com.example.b_rich.data.entities.user
import com.example.b_rich.data.entities.AddAccount
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


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

data class ResetPasswordBody(
    val email: String,
    val code: String,
    val newPassword: String
)
data class ApiResponse(
    val message: String,
    val data: AddAccount
)

interface ApiService {
    // Fetch an account by RIB
    @GET("accounts/rib/{rib}")
    suspend fun getAccountByRIB(@Path("rib") rib: String): Response<Account>

    // Send OTP to the user's email
    @POST("accounts/send-otp")
    suspend fun sendOtp(): Response<ApiResponse>


    // Verify OTP and update nickname
    @POST("accounts/verify-otp")
    suspend fun verifyAndUpdateNickname(
        @Query("rib") rib: String,
        @Query("otp") otp: String,
        @Query("nickname") nickname: String
    ): Response<ApiResponse>

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
}