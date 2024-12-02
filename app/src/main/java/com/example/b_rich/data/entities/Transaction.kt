package com.example.b_rich.data.entities

import java.util.Date

data class Transaction(
    val amount: Double,
    val type: String,
    val wallet: String,
    val createdAt: Date
)
