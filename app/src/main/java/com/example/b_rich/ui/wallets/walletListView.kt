package com.example.b_rich.ui.wallets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.b_rich.data.entities.Wallet
import com.example.b_rich.ui.currency_converter.CurrencyConverterViewModel
import com.example.b_rich.ui.wallets.components.dialogs.AddWalletDialogWrapper
import com.example.b_rich.ui.wallets.components.dialogs.CreateTNDWalletDialog
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import com.example.b_rich.ui.biometricDialog.BiometricAuthenticator
import com.example.b_rich.ui.wallets.components.walletsSection.TNDWalletCard
import com.example.b_rich.ui.wallets.components.transactions.TransactionsSection
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.text.style.TextAlign
import com.example.b_rich.ui.components.SectionTitle
import com.example.b_rich.ui.wallets.components.QuickActionSection.QuickActionsRow
import com.example.b_rich.ui.wallets.components.walletsSection.WalletCard

@Composable
fun Wallets(
    wallets: List<Wallet>,
    TNDWallet: Wallet,
    onWalletSelected: (Wallet) -> Unit,
    selectedWallet: Wallet?,
    currencyConverterViewModel: CurrencyConverterViewModel,
    viewModel: WalletsViewModel,
) {
    val hasTNDWallet = TNDWallet != null

    var showDialog by remember { mutableStateOf(false) }
    var showCreateDialog by remember { mutableStateOf(false) }

    // Configuration de l'authentification biométrique
    val context = LocalContext.current
    val activity = LocalContext.current as FragmentActivity
    val biometricAuthenticator = remember { BiometricAuthenticator(context) }

    // Fonction pour gérer l'authentification biométrique
    fun handleBiometricAuth(action: String) {
        biometricAuthenticator.promptBiometricAuth(
            title = when (action) {
                "add_wallet" -> "Add New Wallet"
                "aliment_wallet" -> "Add Funds to Wallet"
                else -> "Authenticate"
            },
            subTitle = "Please authenticate to continue",
            negativeButtonText = "Cancel",
            fragmentActivity = activity,
            onSuccess = {
                when (action) {
                    "add_wallet" -> showDialog = true
                    "aliment_wallet" -> showCreateDialog = true
                }
            },
            onFailed = {
                Toast.makeText(
                    context,
                    "Authentication failed",
                    Toast.LENGTH_SHORT
                ).show()
            },
            onError = { _, error ->
                Toast.makeText(
                    context,
                    "Error: $error",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                // Section TND Wallet
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                SectionTitle(
                                    title = "My TND Wallet",
                                    description = "Here you can find your TND wallet balance."
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            // Bouton Aliment TND wallet avec authentification
                            Button(
                                onClick = { handleBiometricAuth("aliment_wallet") },
                                modifier = Modifier.wrapContentWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF3D5AFE),
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(20.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = "Aliment TND wallet",
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                            }
                        }

                        TNDWalletCard(
                            wallet = TNDWallet,
                            isSelected = TNDWallet == selectedWallet,
                            onSelect = { onWalletSelected(TNDWallet) }
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }

                // Section Wallets
                item {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            SectionTitle(
                                title = "My Wallets",
                                description = "Here you can find all your wallets and their current balances."
                            )

                            if (wallets.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 24.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AccountBalanceWallet,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(64.dp)
                                                .padding(bottom = 8.dp),
                                            tint = Color(0xFF3D5AFE).copy(alpha = 0.5f)
                                        )

                                        Text(
                                            text = "No Foreign Currency Wallets Yet",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color.Gray
                                        )

                                        Text(
                                            text = "Add your first foreign currency wallet to start managing multiple currencies",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.Gray,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(horizontal = 32.dp)
                                        )

                                        // Bouton Add Foreign Currency Wallet avec authentification
                                        Button(
                                            onClick = { handleBiometricAuth("add_wallet") },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFF3D5AFE),
                                                contentColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(20.dp),
                                            modifier = Modifier
                                                .padding(top = 8.dp)
                                                .height(48.dp)
                                        ) {
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.padding(horizontal = 16.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Add,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                                Text(
                                                    text = "Add Foreign Currency Wallet",
                                                    style = MaterialTheme.typography.labelLarge
                                                )
                                            }
                                        }
                                    }
                                }
                            } else {
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    // Bouton Add Wallet avec authentification
                                    Button(
                                        onClick = { handleBiometricAuth("add_wallet") },
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .height(40.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF3D5AFE),
                                            contentColor = Color.White
                                        ),
                                        shape = RoundedCornerShape(20.dp),
                                        contentPadding = PaddingValues(horizontal = 16.dp)
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = null,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Text(
                                                text = "Add Wallet",
                                                style = MaterialTheme.typography.labelMedium
                                            )
                                        }
                                    }
                                }

                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.padding(top = 16.dp)
                                ) {
                                    items(wallets) { wallet ->
                                        WalletCard(
                                            wallet = wallet,
                                            isSelected = wallet == selectedWallet,
                                            onSelect = { onWalletSelected(wallet) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Quick Actions Section
                if (wallets.isNotEmpty()) {
                    item {
                        SectionTitle(
                            title = "Quick Actions",
                            description = "Select any wallet to perform actions like sending, receiving, withdrawing or topping up your wallet."
                        )
                        QuickActionsRow(
                            selectedWallet = selectedWallet,
                            walletsViewModel = viewModel,
                            currencyConverterViewModel = currencyConverterViewModel,
                        )
                    }
                }

                // Transactions Section
                item {
                    selectedWallet?.let { wallet ->
                        if (wallet != TNDWallet && wallet.transactions.isNotEmpty()) {
                            TransactionsSection(
                                transactions = wallet.transactions,
                                walletPublicKey = wallet.publicKey ?: ""
                            )
                        }
                    }
                }
            }
        }

        // Dialogs
        if (showCreateDialog) {
            CreateTNDWalletDialog(
                onDismiss = { showCreateDialog = false },
                onConfirm = { amount, rib ->  // Ajout du paramètre RIB
                    viewModel.createTNDWallet(amount, rib.toString())
                    showCreateDialog = false
                },
                viewModel = viewModel,
                hasTNDWallet = TNDWallet != null
            )
        }

        if (showDialog) {
            AddWalletDialogWrapper(
                showDialog = true,
                availableCurrencies = listOf("EUR", "USD", "GBP"),
                currencyConverterViewModel = currencyConverterViewModel,
                walletsViewModel = viewModel,
                onDismiss = { showDialog = false }
            )
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