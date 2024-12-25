package com.example.b_rich.ui.wallets.components.transactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.b_rich.data.entities.TransactionSolana


@Composable
fun TransactionSummaryCard(transactions: List<TransactionSolana>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Transaction Summary",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3D5AFE)
            )

            // Calculate statistics
            val totalIncoming = transactions
                .filter { it.toAddress == it.walletPublicKey }
                .sumOf { it.amount }
            val totalOutgoing = transactions
                .filter { it.fromAddress == it.walletPublicKey }
                .sumOf { it.amount }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SummaryItem(
                    title = "Incoming",
                    value = String.format("%.6f SOL", totalIncoming),
                    color = Color(0xFF00C853)
                )
                SummaryItem(
                    title = "Outgoing",
                    value = String.format("%.6f SOL", totalOutgoing),
                    color = Color(0xFFFF1744)
                )
            }
        }
    }
}
