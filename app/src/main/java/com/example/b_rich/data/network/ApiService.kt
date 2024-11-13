package com.example.b_rich.data.network

import com.example.b_rich.data.entities.user
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


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
    val message : String
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


}