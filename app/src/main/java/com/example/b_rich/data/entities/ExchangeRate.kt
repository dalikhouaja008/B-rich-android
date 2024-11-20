package com.example.b_rich.data.entities

data class ExchangeRate(
    val designation: String,
    val code: String,
    val unit: String,
    val buyingRate: String,
    val sellingRate: String,
    val date: String // Format YYYY-MM-DD
)