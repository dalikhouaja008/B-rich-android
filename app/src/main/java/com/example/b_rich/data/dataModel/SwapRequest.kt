package com.example.b_rich.data.dataModel

data class SwapRequest(
    val fromSymbol: String,
    val toSymbol: String,
    val amount: Double,
    val slippage: Double,
    val userPublicKey: String
)