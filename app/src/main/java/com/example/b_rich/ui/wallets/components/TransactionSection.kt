package com.example.b_rich.ui.wallets.components

import TransactionRow
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.b_rich.data.entities.TransactionSolana

@Composable
fun TransactionsSection(
    transactions: List<TransactionSolana>,
    walletPublicKey: String
) {
    var showAllTransactions by remember { mutableStateOf(false) }
    val displayedTransactions = if (showAllTransactions) {
        transactions
    } else {
        transactions.take(5)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header with transaction count
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionTitle(
                title = "Wallet Transactions",
                description = "${transactions.size} transactions in total"
            )

            // Filter or sort options could be added here
            IconButton(onClick = { /* Add filter/sort logic */ }) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter transactions",
                    tint = Color(0xFF3D5AFE)
                )
            }
        }

        // Transactions list
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(), // Smooth animation when expanding/collapsing
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            displayedTransactions.forEach { transaction ->
                TransactionRow(transaction = transaction)
            }
        }

        // "View More" button - only show if there are more than 5 transactions
        if (transactions.size > 5) {
            Button(
                onClick = { showAllTransactions = !showAllTransactions },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3D5AFE).copy(alpha = 0.1f),
                    contentColor = Color(0xFF3D5AFE)
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.elevatedButtonElevation(
                    defaultElevation = 0.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (showAllTransactions) "Show Less" else "View All Transactions",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        imageVector = if (showAllTransactions)
                            Icons.Default.KeyboardArrowUp
                        else
                            Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(20.dp)
                    )
                }
            }
        }

        // Transaction summary card
        TransactionSummaryCard(transactions = transactions)
    }
}