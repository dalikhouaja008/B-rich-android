package com.example.b_rich.data.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    companion object {
        @Volatile
        private var token: String? = null

        fun setToken(newToken: String) {
            token = newToken
        }

        fun getToken() = token
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // N'ajouter le token que s'il est explicitement défini
        val authenticatedRequest = token?.let {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $it")
                .build()
        } ?: originalRequest

        return chain.proceed(authenticatedRequest)
    }
}