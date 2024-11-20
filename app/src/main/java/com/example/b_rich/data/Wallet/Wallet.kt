package com.example.b_rich.data.Wallet

import com.example.b_rich.data.Transaction.Transaction
import java.util.UUID


data class Wallet(
    val id: UUID = UUID.randomUUID(),
    val currency: String,
    val balance: Double,
    val symbol: String,
    val transactions: List<Transaction> = emptyList()
)

/*
data class Wallet(
    val currency: String,
    val symbol: String,
    val balance: Double
)
 */
