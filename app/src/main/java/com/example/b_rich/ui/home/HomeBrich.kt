package com.example.b_rich.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.ui.draw.shadow
import com.example.b_rich.data.Wallet.Wallet
import com.example.b_rich.data.Transaction.Transaction
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeBricScreen(
    totalBalance: Double,
    wallets: List<Wallet>,
    recentTransactions: List<Transaction>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Total Balance Section
        TotalBalanceCard(totalBalance)

        // Wallets Section
        Text(
            text = "My Wallets",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        WalletsCarousel(wallets)

        // Quick Actions Section
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        QuickActionsRow()

        // Recent Transactions Section
        Text(
            text = "Recent Transactions",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        TransactionsList(recentTransactions)
    }
}

@Composable
fun TotalBalanceCard(totalBalance: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total Balance",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = "$${"%.2f".format(totalBalance)}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun WalletsCarousel(wallets: List<Wallet>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(wallets) { wallet ->
            WalletCard(wallet)
        }
    }
}

@Composable
fun WalletCard(wallet: Wallet) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = wallet.currency,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "${wallet.symbol}${"%.2f".format(wallet.balance)}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun QuickActionsRow(
    actions: List<QuickAction> = listOf(
        QuickAction(Icons.Default.Send, "Send"),
        QuickAction(Icons.Default.Download, "Receive"),
        QuickAction(Icons.Default.QrCode, "Scan"),
        QuickAction(Icons.Default.CreditCard, "Pay")
    )
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        actions.forEach { action ->
            QuickActionButton(action.icon, action.label)
        }
    }
}

@Composable
fun QuickActionButton(icon: ImageVector, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .size(80.dp)
            .padding(8.dp)
            .shadow(8.dp, shape = MaterialTheme.shapes.medium)
            .background(
                MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.medium
            )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.size(36.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun TransactionsList(transactions: List<Transaction>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(transactions) { transaction ->
            TransactionRow(transaction)
        }
    }
}

@Composable
fun TransactionRow(transaction: Transaction) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = transaction.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${if (transaction.amount < 0) "-" else ""}$${"%.2f".format(transaction.amount)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (transaction.amount < 0)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.primary
                )
                Text(
                    text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(transaction.date),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

data class QuickAction(
    val icon: ImageVector,
    val label: String
)

@Preview(showBackground = true)
@Composable
fun PreviewHomeBricScreen() {
    val sampleWallets = listOf(
        Wallet(currency = "USD", symbol = "$", balance = 500.00, transactions = emptyList()),
        Wallet(currency = "EUR", symbol = "€", balance = 300.00, transactions = emptyList()),
        Wallet(currency = "BTC", symbol = "₿", balance = 0.1, transactions = emptyList())
    )
    val sampleTransactions = listOf(
        Transaction(id = 1, status = "Completed", description = "Grocery", amount = -50.00, date = Date()),
        Transaction(id = 2, status = "Completed", description = "Salary", amount = 2000.00, date = Date()),
        Transaction(id = 3, status = "Pending", description = "Coffee", amount = -5.00, date = Date())
    )
    HomeBricScreen(
        totalBalance = 1234.56,
        wallets = sampleWallets,
        recentTransactions = sampleTransactions
    )
}
