package com.example.b_rich.data.entities

import java.util.UUID


data class Wallet(
    val id: UUID = UUID.randomUUID(),
    val currency: String,
    val balance: Double,
    val symbol: String,
    val transactions: List<Transaction> = emptyList()
)