package com.example.b_rich.data.entities

import java.util.Date

data class TransactionSolana (
    val id: String,
    val signature: String,
    val walletPublicKey: String,
    val userId: String,
    val fromAddress: String?,
    val toAddress: String?,
    val amount: Double,
    val blockTime: Long,
    val status: String,
    val type: String,
    val timestamp: Date,
    val fee: Double
)

