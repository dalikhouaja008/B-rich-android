package com.example.b_rich.data.repositories

import com.example.b_rich.data.dataModel.NewsItem
import com.example.b_rich.data.dataModel.PredictionResponse
import com.example.b_rich.data.entities.ExchangeRate
import com.example.b_rich.data.network.ApiService
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class ExchangeRateRepository @Inject constructor(
    private val apiService: ApiService
){
    suspend fun fetchExchangeRates():Response<List<ExchangeRate>> {
        return try{
            apiService.getExchangeRate()
        }catch (e: Exception){
            e.printStackTrace()
            Response.error(500, ResponseBody.create(null, "Error creating user"))
        }
    }

     suspend fun getttingAllNews(): List<NewsItem> {
        return try {
            apiService.getAllNews()
        } catch (e: Exception) {
            // Gestion des erreurs
            emptyList()
        }
    }

}