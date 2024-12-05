package com.example.b_rich.ui.wallets

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.b_rich.data.entities.Wallet
import com.example.b_rich.data.entities.Transaction
import com.example.b_rich.ui.currency_converter.CurrencyConverterViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WalletsScreen(viewModel: WalletsViewModel,currencyConverterViewModel: CurrencyConverterViewModel ) {
    val wallets by viewModel.wallets.collectAsState()
    val recentTransactions by viewModel.recentTransactions.collectAsState()

    // State to track the currently selected wallet
    var selectedWallet by remember { mutableStateOf<Wallet?>(null) }

    LaunchedEffect(key1 = true) {
        viewModel.fetchWallets()
    }

    Wallets(
        wallets = wallets,
        recentTransactions = recentTransactions,
        onWalletSelected = { wallet -> selectedWallet = wallet },
        selectedWallet = selectedWallet,
        currencyConverterViewModel = currencyConverterViewModel,
        viewModel= viewModel
    )
}


@Composable
fun Wallets(
    wallets: List<Wallet>,
    recentTransactions: List<Transaction>,
    onWalletSelected: (Wallet) -> Unit,
    selectedWallet: Wallet?,
    currencyConverterViewModel: CurrencyConverterViewModel,
    viewModel: WalletsViewModel,
) {
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFFFFF),
            Color(0xFF2196F3)
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
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    SectionTitle("My Wallets")
                    WalletsCarousel(
                        wallets = wallets,
                        onWalletSelected = onWalletSelected,
                        selectedWallet = selectedWallet
                    )
                }
                item {
                    SectionTitle("Quick Actions")
                    QuickActionsRow(
                        selectedWallet = selectedWallet,
                        walletsViewModel= viewModel,
                        currencyConverterViewModel = currencyConverterViewModel
                    )
                }
                item {
                    SectionTitle("Recent Transactions")
                }
                items(recentTransactions) { transaction ->
                    TransactionRow(transaction)
                }
            }
        }
    }
}
@Composable
fun WalletsCarousel(
    wallets: List<Wallet>,
    onWalletSelected: (Wallet) -> Unit,
    selectedWallet: Wallet?
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(wallets) { wallet ->
            WalletCard(
                wallet = wallet,
                isSelected = selectedWallet == wallet,
                onSelect = { onWalletSelected(wallet) }
            )
        }
    }
}

@Composable
fun WalletCard(
    wallet: Wallet,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val elevation by animateDpAsState(if (isSelected) 12.dp else 4.dp)

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
            .clickable { onSelect() }
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${"%.2f".format(wallet.balance)} SOL",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Add selection indicator
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .background(Color.White)
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
    selectedWallet: Wallet?,
    walletsViewModel: WalletsViewModel,
    currencyConverterViewModel: CurrencyConverterViewModel,
    actions: List<QuickAction> = listOf(
        QuickAction(Icons.Default.Send, "Send"),
        QuickAction(Icons.Default.Download, "Receive"),
        QuickAction(Icons.Default.Add, "Aliment"),
        QuickAction(Icons.Default.CreditCard, "Pay")
    )
) {
    var showAlimentDialog by remember { mutableStateOf(false) }
    val uiState by currencyConverterViewModel.uiStateCurrency.collectAsState()
    val convertedWallet by walletsViewModel.convertedWallet.collectAsState()
    val conversionError by walletsViewModel.conversionError.collectAsState()
    val isLoading by walletsViewModel.isLoading.collectAsState()
    var showSendDialog by remember { mutableStateOf(false) }
    val sendTransactionState by walletsViewModel.sendTransactionState.collectAsState()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        actions.forEach { action ->
            QuickActionButton(
                icon = action.icon,
                label = action.label,
                onClick = {
                    when (action.label) {
                        "Aliment" -> {
                            if (selectedWallet != null) {
                                showAlimentDialog = true
                            }
                        }
                        "Send" -> {
                            if (selectedWallet != null) {
                                showSendDialog = true
                            }
                        }
                        else -> { /* Handle other actions */ }
                    }
                }
            )
        }
    }

    // Wallet Alimentation Dialog
    if (showAlimentDialog && selectedWallet != null) {
        var dinarsAmount by remember { mutableStateOf("") }
        var convertedAmount by remember { mutableStateOf("0.0") }
        var userConfirmed by remember { mutableStateOf(false) }
        var alimentButtonEnabled by remember { mutableStateOf(false) }
        var conversionInProgress by remember { mutableStateOf(false) }

        // Conversion Effect
        LaunchedEffect(dinarsAmount) {
            if (dinarsAmount.isNotEmpty()) {
                userConfirmed = false
                alimentButtonEnabled = false
                conversionInProgress = true
                // Always convert from TND to the wallet's currency
                currencyConverterViewModel.calculateSellingRate(
                    currency = selectedWallet.currency,
                    amount = dinarsAmount
                )
            }
        }

        // Update converted amount when conversion rate changes
        LaunchedEffect(uiState.convertedAmount) {
            if (uiState.convertedAmount > 0) {
                convertedAmount = "%.2f".format(uiState.convertedAmount)
                conversionInProgress = false
            }
        }

        // React to successful wallet conversion
        LaunchedEffect(convertedWallet) {
            convertedWallet?.let {
                showAlimentDialog = false
            }
        }

        AlertDialog(
            onDismissRequest = {
                showAlimentDialog = false
            },
            title = { Text("Alimenter ${selectedWallet.currency} Wallet") },
            text = {
                Column {
                    TextField(
                        value = dinarsAmount,
                        onValueChange = {
                            dinarsAmount = it
                        },
                        label = { Text("Amount in Dinars") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        isError = conversionError != null
                    )

                    if (conversionError != null) {
                        Text(
                            text = conversionError ?: "Conversion Error",
                            color = Color.Red
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = convertedAmount,
                        onValueChange = { },
                        label = { Text("Converted Amount") },
                        readOnly = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amount = dinarsAmount.toDoubleOrNull()
                        if (amount != null && amount > 0) {
                            walletsViewModel.convertCurrency(
                                amount = convertedAmount.toDoubleOrNull() ?: 0.0,
                                fromCurrency = selectedWallet.currency
                            )
                        } else {
                            // Vous pouvez afficher un message d'erreur ici si nécessaire
                            println("Montant invalide ou vide")
                        }
                    },
                    enabled = true // Toujours cliquable
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White
                        )
                    } else {
                        Text("Alimenter")
                    }
                }

            },
            dismissButton = {
                Button(
                    onClick = {
                        showAlimentDialog = false
                        dinarsAmount = "" // Champ du montant en dinars
                        convertedAmount = "0.0" // Montant converti
                        userConfirmed = false // Confirmation utilisateur
                        alimentButtonEnabled = false // Bouton d'alimentation désactivé
                        conversionInProgress = false // Conversion en cours réinitialisée

                    }
                ) {
                    Text("Cancel")
                }
            }
        )
            // Wallet Send Funds Dialog

        }
    if (showSendDialog && selectedWallet != null) {
        if (showSendDialog) {
            var recipientAddress by remember { mutableStateOf("") }
            var sendAmount by remember { mutableStateOf("") }
            var sendError by remember { mutableStateOf<String?>(null) }

            AlertDialog(
                onDismissRequest = {
                    showSendDialog = false
                },
                title = { Text("Send Funds from ${selectedWallet?.currency} Wallet") },
                text = {
                    Column {
                        TextField(
                            value = recipientAddress,
                            onValueChange = {
                                recipientAddress = it
                            },
                            label = { Text("Recipient Address") },
                            isError = sendError != null
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        TextField(
                            value = sendAmount,
                            onValueChange = {
                                sendAmount = it
                            },
                            label = { Text("Amount to Send") },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number
                            ),
                            isError = sendError != null
                        )

                        if (sendError != null) {
                            Text(
                                text = sendError ?: "Invalid input",
                                color = Color.Red
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val amount = sendAmount.toDoubleOrNull()
                            if (recipientAddress.isNotEmpty() && amount != null && amount > 0) {
                                walletsViewModel.sendTransaction(
                                    fromWalletPublicKey = selectedWallet.publicKey,
                                    toWalletPublicKey = recipientAddress,
                                    amount = amount
                                )
                                showSendDialog = false
                            } else {
                                sendError = "Please provide valid recipient and amount."
                            }
                        },
                        enabled = true // Toujours cliquable
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White
                            )
                        } else {
                            Text("Send")
                        }
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showSendDialog = false
                            recipientAddress = ""
                            sendAmount = ""
                            sendError = null
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
    }
@Composable
fun QuickActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.2f))
            .clickable { onClick() }
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

/*@Preview(showBackground = true, device = "spec:width=412dp,height=892dp", backgroundColor = 0xFF1A73E8)
@Composable
fun PreviewWallets(viewModel: WalletsViewModel = WalletsViewModel()) {
    WalletsScreen(viewModel)
}*/