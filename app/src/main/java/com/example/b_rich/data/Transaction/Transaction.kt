package com.example.b_rich.data.Transaction

import java.util.Date

data class Transaction(
    val id: Int,
    val status: String,
    val description: String,
    val amount: Double,
    val date: Date
)
