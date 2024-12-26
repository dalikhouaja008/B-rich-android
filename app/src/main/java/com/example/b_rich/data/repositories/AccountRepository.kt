package com.example.b_rich.data.repositories

import android.util.Log
import com.example.b_rich.data.dataModel.LinkAccountRequest
import com.example.b_rich.data.entities.CustomAccount
import com.example.b_rich.data.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AccountRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun fetchAccounts(): List<CustomAccount> = withContext(Dispatchers.IO) {
        val response = apiService.getAllAccounts()
        if (response.isSuccessful) {
            response.body() ?: emptyList()
        } else {
            throw Exception("Failed to fetch accounts: ${response.errorBody()?.string()}")
        }
    }

    suspend fun setDefaultAccount(accountId: String) = withContext(Dispatchers.IO) {
        val response = apiService.setDefaultAccount(accountId)
        if (!response.isSuccessful) {
            throw Exception("Failed to set default account: ${response.errorBody()?.string()}")
        }
    }

    suspend fun linkAccount(rib: String, nickname: String?): Result<CustomAccount> =
        withContext(Dispatchers.IO) {
            try {
                val request = LinkAccountRequest(rib = rib, nickname = nickname)
                Log.d("AccountRepository", "Sending request: $request")

                val response = apiService.linkAccount(request)
                Log.d("AccountRepository", "Response code: ${response.code()}")

                if (response.isSuccessful) {
                    Result.success(response.body() ?: throw Exception("Empty response"))
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("AccountRepository", "Error body: $errorBody")

                    val errorMessage = when (response.code()) {
                        500 -> "Server error: ${errorBody ?: "Unknown server error"}"
                        401 -> "Session expired"
                        403 -> "Not authorized"
                        404 -> "Account not found"
                        409 -> "Account already linked"
                        else -> errorBody ?: "Unknown error"
                    }
                    Result.failure(Exception(errorMessage))
                }
            } catch (e: Exception) {
                Log.e("AccountRepository", "Exception during link account", e)
                Result.failure(e)
            }
        }
}