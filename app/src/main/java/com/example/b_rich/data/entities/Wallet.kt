package com.example.b_rich.data.entities

import java.util.UUID


import java.util.Date


data class Wallet(
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val publicKey: String,
    val privateKey: String,
    val type: String,
    val network: String,
    val balance: Double,
    val createdAt: Date = Date(),
    val currency: String,
    val originalAmount: Double,
    val convertedAmount: Double
)