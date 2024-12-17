package com.example.b_rich.data.entities

data class Account(
    val id: Int,
    val userId: String,
    val type: String,
    val balance: Double,
    val status: String
)