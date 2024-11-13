package com.example.b_rich.data.repositories

import com.example.b_rich.data.entities.user
import com.example.b_rich.data.network.ApiService
import com.example.b_rich.data.network.LoginRequest
import com.example.b_rich.data.network.LoginResponse
import com.example.b_rich.data.network.RequestResetBody
import com.example.b_rich.data.network.ResetPasswordBody
import com.example.b_rich.data.network.ResponseReset
import com.example.b_rich.data.network.VerifyCodeBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body

public class UserRepository(private val apiService: ApiService) {

    suspend fun createUser(user: user): Response<user> {
        return try {
            apiService.createUser(user)
        } catch (e: Exception) {
            // Log the error for debugging
            e.printStackTrace()
            Response.error(500, ResponseBody.create(null, "Error creating user"))
        }
    }
    suspend fun login(email: String, password: String): Response<LoginResponse> {
        return try {
            apiService.login(LoginRequest(email, password))
        } catch (e: Exception) {
            e.printStackTrace()
            Response.error(500, ResponseBody.create(null, "Error logging in"))
        }
    }

    suspend fun loginWithBiometric(email: String, password: String): Response<LoginResponse> {
        return try {
            apiService.loginwithbiometric(LoginRequest(email, password))
        } catch (e: Exception) {
            e.printStackTrace()
            Response.error(500, ResponseBody.create(null, "Error logging in with biometric"))
        }
    }

    suspend fun requestReset(email: String): Response<ResponseReset> {
        return try {
            apiService.requestReset(RequestResetBody(email))
        } catch (e: Exception) {
            e.printStackTrace()
            Response.error(500, ResponseBody.create(null, "Error requesting password reset"))
        }
    }

    suspend fun verifyCode(email: String, code: String): Response<ResponseReset> {
        return try {
            apiService.verifyCode(VerifyCodeBody(email, code))
        } catch (e: Exception) {
            e.printStackTrace()
            Response.error(500, ResponseBody.create(null, "Error verifying code"))
        }
    }

    suspend fun resetPassword(email: String, code: String, newPassword: String): Response<ResponseReset> {
        return try {
            apiService.resetPassword(ResetPasswordBody(email, code, newPassword))
        } catch (e: Exception) {
            e.printStackTrace()
            Response.error(500, ResponseBody.create(null, "Error resetting password"))
        }
    }
}

