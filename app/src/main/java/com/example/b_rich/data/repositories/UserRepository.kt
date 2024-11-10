package com.example.b_rich.data.repositories

import com.example.b_rich.data.entities.user
import com.example.b_rich.data.network.ApiService
import com.example.b_rich.data.network.LoginRequest
import com.example.b_rich.data.network.LoginResponse
import okhttp3.ResponseBody
import retrofit2.Response

public class UserRepository(private val apiService: ApiService) {
    // Function to create a new user using the API with error handling
    suspend fun createUser(user: user): Response<user> {
        return try {
            apiService.createUser(user)
        } catch (e: Exception) {
            // Log the error for debugging
            e.printStackTrace()
            Response.error(500, ResponseBody.create(null, "Error creating user"))
        }
    }
    // Add login function
    suspend fun login(email: String, password: String): Response<LoginResponse> {
        return apiService.login(LoginRequest(email, password))
    }

    suspend fun loginwithbiometric(email: String,password: String):Response<LoginResponse>{
        return apiService.loginwithbiometric(LoginRequest(email,password))
    }

}