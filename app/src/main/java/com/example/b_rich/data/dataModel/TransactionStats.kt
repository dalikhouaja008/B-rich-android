package com.example.b_rich.data.dataModel

data class TransactionStats(
    val totalTransactions: Int,
    val totalIncoming: Double,
    val totalOutgoing: Double,
    val averageAmount: Double
)