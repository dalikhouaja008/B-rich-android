package com.example.b_rich.ui.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.b_rich.data.Wallet.Wallet
import com.example.b_rich.data.Transaction.Transaction
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeBrichScreen(
    totalBalance: Double,
    wallets: List<Wallet>,
    recentTransactions: List<Transaction>
) {
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE91E63),
            Color(0xFF00BCD4)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            UserHeader()
            TotalBalanceCard(totalBalance)

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    SectionTitle("Quick Actions")
                    QuickActionsRow()
                }

                item {
                    SectionTitle("My Wallets")
                    WalletsCarousel(wallets)
                }
/*
                item {
                    SectionTitle("Recent Transactions")
                }



                items(recentTransactions) { transaction ->
                    TransactionRow(transaction)
                }

 */
            }
        }
    }
}

@Composable
fun UserHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Hello, User",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Welcome back to B-Rich",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )
        }
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun TotalBalanceCard(totalBalance: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Total Balance",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.DarkGray
            )
            Text(
                text = "${"%.2f".format(totalBalance)} USD",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

@Composable
fun WalletsCarousel(wallets: List<Wallet>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(wallets) { wallet ->
            WalletCard(wallet)
        }
    }
}

@Composable
fun WalletCard(wallet: Wallet) {
    var isExpanded by remember { mutableStateOf(false) }
    val elevation by animateDpAsState(if (isExpanded) 12.dp else 4.dp)

    // Assign different gradient colors for each currency
    val gradientBackground = when (wallet.currency) {
        "Tunisian Dinar" -> Brush.horizontalGradient(
            colors = listOf(Color(0xFFFFC107), Color(0xFFFFD54F)) // gold tones for dinars
        )
        "Euro" -> Brush.horizontalGradient(
            colors = listOf(Color(0xFF1976D2), Color(0xFF64B5F6)) // blue tones for euros
        )
        "US Dollar" -> Brush.horizontalGradient(
            colors = listOf(Color(0xFF4CAF50), Color(0xFF81C784)) // green tones for dollars
        )
        else -> Brush.horizontalGradient(
            colors = listOf(Color.LightGray, Color.Gray)
        )
    }

    Card(
        modifier = Modifier
            .width(260.dp)
            .clickable { isExpanded = !isExpanded }
            .clip(RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(elevation),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .background(gradientBackground)
                .padding(20.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = wallet.currency,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Text(
                    text = "${wallet.symbol}${"%.2f".format(wallet.balance)}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                if (isExpanded) {
                    Text(
                        text = "Last updated: ${SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}
@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = Color.White,
        modifier = Modifier.padding(vertical = 8.dp)
    )
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
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.2f))
            .clickable { /* Action implementation */ }
            .padding(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White
        )
    }
}

@Composable
fun TransactionRow(transaction: Transaction) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
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
                    color = Color.White
                )
                Text(
                    text = "${if (transaction.amount < 0) "-" else ""}${"%.2f".format(transaction.amount)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (transaction.amount < 0) Color(0xFFFF6B6B) else Color(0xFF4CAF50)
                )
                Text(
                    text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(transaction.date),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }
    }
}

data class QuickAction(
    val icon: ImageVector,
    val label: String
)

@Preview(showBackground = true, device = "spec:width=412dp,height=892dp", backgroundColor = 0xFF1A73E8)
@Composable
fun PreviewHomeBrichScreen() {
    val sampleWallets = listOf(
        Wallet(
            currency = "Tunisian Dinar",
            symbol = "TND",
            balance = 2500.00,
            transactions = emptyList()
        ),
        Wallet(
            currency = "Euro",
            symbol = "€",
            balance = 800.00,
            transactions = emptyList()
        ),
        Wallet(
            currency = "US Dollar",
            symbol = "$",
            balance = 1500.00,
            transactions = emptyList()
        )
    )
    val sampleTransactions = listOf(
        Transaction(id = 1, status = "Completed", description = "Deposit", amount = 200.00, date = Date()),
        Transaction(id = 2, status = "Completed", description = "Shopping", amount = -50.00, date = Date())
    )
    HomeBrichScreen(
        totalBalance = 4800.00,
        wallets = sampleWallets,
        recentTransactions = sampleTransactions
    )
}