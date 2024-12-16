package com.example.b_rich.data.entities

import java.util.UUID


import java.util.Date


data class Wallet(
    val id: String,
    val userId: String,
    val publicKey: String,
    val privateKey:String,
    val type:String,
    val network:String,
    val balance: Double,
    val createdAt: Date,
    val currency: String,
    val originalAmount:Number,
    val convertedAmount:Number
)
