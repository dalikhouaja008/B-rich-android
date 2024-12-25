package com.example.b_rich.ui.wallets.components.transactions

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.b_rich.data.dataModel.TransactionStats

@SuppressLint("DefaultLocale")
@Composable
fun StatsCard(stats: TransactionStats) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Transaction Statistics",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    label = "Total",
                    value = stats.totalTransactions.toString(),
                    color = Color(0xFF3D5AFE)
                )
                StatItem(
                    label = "Incoming",
                    value = String.format("%.2f SOL", stats.totalIncoming),
                    color = Color(0xFF00C853)
                )
                StatItem(
                    label = "Outgoing",
                    value = String.format("%.2f SOL", stats.totalOutgoing),
                    color = Color(0xFFFF1744)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Average Amount: ${String.format("%.2f SOL", stats.averageAmount)}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}
