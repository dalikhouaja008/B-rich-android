package com.example.b_rich.data.entities

import java.util.Date

data class Transaction(
    val id: Int,
    val status: String,
    val description: String,
    val amount: Double,
    val date: Date
)