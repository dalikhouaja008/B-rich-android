package com.example.b_rich.ui.wallets.components.transactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.b_rich.data.entities.TransactionSolana
import com.example.b_rich.ui.components.SectionTitle


// Créez d'abord un enum pour les filtres
enum class TransactionFilter(val label: String) {
    ALL("All"),
    SENT("Sent"),
    RECEIVED("Received")
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun TransactionsSection(
    transactions: List<TransactionSolana>,
    walletPublicKey: String
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf(TransactionFilter.ALL) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredTransactions = remember(selectedFilter, searchQuery, transactions) {
        transactions.filter { transaction ->
            val matchesFilter = when (selectedFilter) {
                TransactionFilter.ALL -> true
                TransactionFilter.SENT -> transaction.fromAddress == walletPublicKey
                TransactionFilter.RECEIVED -> transaction.toAddress == walletPublicKey
            }

            val matchesSearch = searchQuery.isEmpty() ||
                    transaction.type.contains(searchQuery, ignoreCase = true) ||
                    transaction.status.contains(searchQuery, ignoreCase = true)

            matchesFilter && matchesSearch
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // En-tête avec le nombre de transactions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionTitle(
                title = "Wallet Transactions",
                description = "${transactions.size} transactions in total"
            )
        }

        // Afficher les 6 premières transactions
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            transactions.take(6).forEach { transaction ->
                TransactionRow(transaction = transaction)
            }
        }

        // Bouton View More
        if (transactions.size > 6) {
            Button(
                onClick = { showBottomSheet = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3D5AFE)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "View All Transactions",
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }

    // BottomSheet pour les transactions
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            TransactionsBottomSheet(
                transactions = filteredTransactions,
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it },
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                onDismiss = { showBottomSheet = false }
            )
        }
    }
}
