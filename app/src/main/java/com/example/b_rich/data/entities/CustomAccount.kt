package com.example.b_rich.data.entities

data class CustomAccount(
    val id: String? = null,
    val rib: String,
    val name: String? = null,
    val balance: Double? = 0.0,
    val status: String? = "active",
    val isDefault: Boolean? = false,
    val nickname: String? = null
)