package com.example.b_rich.ui.wallets

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.b_rich.data.entities.Transaction
import com.example.b_rich.data.entities.Wallet
import com.example.b_rich.ui.wallets.WalletsViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun WalletsScreen(viewModel: WalletsViewModel = hiltViewModel()) {
    val wallets = viewModel.wallets.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (wallets.isNotEmpty()) {
            wallets.forEach { wallet ->
                Text(text = "Wallet Name: ${wallet.walletName}, Balance: ${wallet.balance}")
            }
        } else {
            Text(text = "No wallets available.")
        }
    }
}




@Composable
fun Wallets(
    totalBalance: Double,
    wallets: List<Wallet>,
    recentTransactions: List<Transaction>
) {
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFFFFF), // Couleur de fond blanche
            Color(0xFF2196F3)  // Couleur de fond bleue pour le bas
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
                    SectionTitle("Actions rapides")
                    QuickActionsRow()
                }

                item {
                    SectionTitle("Mes Portefeuilles")
                    WalletsCarousel(wallets)
                }

                item {
                    SectionTitle("Transactions Récentes")
                }
                items(recentTransactions) { transaction ->
                    TransactionRow(transaction)
                }
            }
        }
    }
}

@Composable
fun UserHeader() {
    // Implémentation de l'entête de l'utilisateur
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
                text = "Solde Total",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.DarkGray
            )
            Text(
                text = "${"%.2f".format(totalBalance)} USD", // Formatez la balance en USD
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

    // Affecter différentes couleurs de dégradé pour chaque portefeuille
    val gradientBackground = when (wallet.walletName) {
        "Tunisian Dinar" -> Brush.horizontalGradient(
            colors = listOf(Color(0xFFFFC107), Color(0xFFFFD54F)) // Or pour dinars
        )
        "Euro" -> Brush.horizontalGradient(
            colors = listOf(Color(0xFF1976D2), Color(0xFF64B5F6)) // Bleu pour euros
        )
        "US Dollar" -> Brush.horizontalGradient(
            colors = listOf(Color(0xFF4CAF50), Color(0xFF81C784)) // Vert pour dollars
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
                        text = wallet.walletName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Text(
                    text = "${"%.2f".format(wallet.balance)}", // Affiche le solde du portefeuille
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                if (isExpanded) {
                    Text(
                        text = "Dernière mise à jour : ${SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())}",
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
        QuickAction(Icons.Default.Send, "Envoyer"),
        QuickAction(Icons.Default.Download, "Recevoir"),
        QuickAction(Icons.Default.QrCode, "Scanner"),
        QuickAction(Icons.Default.CreditCard, "Payer")
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
            .clickable { /* Implémenter action */ }
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
                    text = transaction.type,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
                Text(
                    text = "${if (transaction.amount < 0) "-" else ""}${"%.2f".format(transaction.amount)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (transaction.amount < 0) Color(0xFFFF6B6B) else Color(0xFF4CAF50)
                )
                Text(
                    text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(transaction.createdAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }
    }
}


@Composable
fun TransactionDetails(transaction: Transaction) {
    Text(
        text = transaction.type,
        style = MaterialTheme.typography.bodyLarge,
        color = Color.White
    )
    Text(
        text = formatTransactionAmount(transaction.amount),
        style = MaterialTheme.typography.bodyMedium,
        color = if (transaction.amount < 0) Color(0xFFFF6B6B) else Color(0xFF4CAF50)
    )
    Text(
        text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(transaction.createdAt),
        style = MaterialTheme.typography.bodySmall,
        color = Color.White.copy(alpha = 0.7f)
    )
}

fun formatTransactionAmount(amount: Double): String {
    return "${if (amount < 0) "-" else ""}${"%.2f".format(kotlin.math.abs(amount))}"
}

data class QuickAction(
    val icon: ImageVector,
    val label: String
)

/*
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WalletsScreen()
}
 */

@Preview(showBackground = true, device = "spec:width=412dp,height=892dp", backgroundColor = 0xFF1A73E8)
@Composable
fun PreviewWallets() {
    WalletsScreen()
}
