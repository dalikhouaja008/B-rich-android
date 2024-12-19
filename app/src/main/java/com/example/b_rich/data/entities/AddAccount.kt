package com.example.b_rich.data.entities

data class AddAccount(
    val rib: String,
    val name: String,
    val balance: Double,
    val isDefault: Boolean = false
)