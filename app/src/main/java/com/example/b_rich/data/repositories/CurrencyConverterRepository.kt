package com.example.b_rich.data.repositories

import com.example.b_rich.data.dataModel.PredictionRequest
import com.example.b_rich.data.dataModel.PredictionResponse
import com.example.b_rich.data.network.ApiService
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class CurrencyConverterRepository @Inject constructor(
    private val apiService: ApiService ){

    suspend fun getSellingRate(currency: String, amount: String): Double {
        return try {
            val result = apiService.getSellingRate(currency, amount)
            println("Selling rate result: $result")
            result
        } catch (e: Exception) {
            println("Error getting selling rate: ${e.message}")

            // Additional error handling or logging
            0.0 // or throw the exception, depending on your error handling strategy
        }
    }

    suspend fun getBuyingRate(currency: String, amount: String): Double {
        return try {
            apiService.getBuyingRate(currency, amount)
        } catch (e: Exception) {
            println("Error getting buying rate: ${e.message}")
            0.0 // or throw the exception, depending on your error handling strategy
        }
    }


    suspend fun getCurrencyPredictions(
        date: String,
        currencies: List<String>
    ): Response<PredictionResponse> {
        return try {
            apiService.getPredictions(PredictionRequest(date, currencies))
        } catch (e: Exception) {
            e.printStackTrace()
            Response.error(500, ResponseBody.create(null, "Error fetching currency predictions"))
        }
    }
}