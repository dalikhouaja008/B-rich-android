package com.example.b_rich.data.repositories

import com.example.b_rich.data.dataModel.ForgotPasswordRequest
import com.example.b_rich.data.dataModel.ForgotPasswordResponse
import com.example.b_rich.data.dataModel.VerifyCodeResponse
import com.example.b_rich.data.entities.user
import com.example.b_rich.data.network.ApiService
import com.example.b_rich.data.network.LoginRequest
import com.example.b_rich.data.network.LoginResponse
import com.example.b_rich.data.network.RequestResetBody
import com.example.b_rich.data.network.RequestResetResponse
import com.example.b_rich.data.network.ResetPasswordBody
import com.example.b_rich.data.network.ResponseReset
import com.example.b_rich.data.network.VerifyCodeBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import javax.inject.Inject

public class UserRepository @Inject constructor(
    private val apiService: ApiService
)  {

    suspend fun createUser(user: user): Response<user> {
        return try {
            apiService.createUser(user)
        } catch (e: Exception) {
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

    suspend fun requestPasswordReset(email: String): Response<RequestResetResponse> {
        return try {
            apiService.requestReset(RequestResetBody(email))
        } catch (e: Exception) {
            e.printStackTrace()
            Response.error(500, ResponseBody.create(null, "Error requesting password reset"))
        }
    }

    suspend fun forgotPassword(email: String): Response<ForgotPasswordResponse> {
        return try {
            apiService.forgotPassword(ForgotPasswordRequest(email))
        } catch (e: Exception) {
            e.printStackTrace()
            Response.error(500, ResponseBody.create(null, "Error requesting forget password"))
        }
    }


    suspend fun verifyCode(email: String, code: String): Response<VerifyCodeResponse> {
        return apiService.verifyCode(VerifyCodeBody(email, code))
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

