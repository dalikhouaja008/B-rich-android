package com.example.b_rich.ui.wallets.components.walletsSection

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.b_rich.data.entities.Wallet
import com.example.b_rich.ui.currency_converter.CurrencyConverterViewModel
import com.example.b_rich.ui.wallets.WalletsViewModel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.fragment.app.FragmentActivity
import com.example.b_rich.ui.biometricDialog.BiometricAuthenticator
import com.example.b_rich.ui.wallets.Wallets
import com.example.b_rich.ui.wallets.components.dialogs.CreateTNDWalletDialog

@Composable
fun WalletsScreen(
    viewModel: WalletsViewModel,
    currencyConverterViewModel: CurrencyConverterViewModel
) {
    val tndWallet by viewModel.tndWallet.collectAsState()
    val currencyWallets by viewModel.currencyWallets.collectAsState()
    val recentTransactions by viewModel.recentTransactions.collectAsState()
    var selectedWallet by remember { mutableStateOf<Wallet?>(null) }
    val hasResponse by viewModel.hasResponse.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }

    // Ajouter le contexte et l'authentificateur biométrique
    val context = LocalContext.current
    val activity = LocalContext.current as FragmentActivity
    val biometricAuthenticator = remember { BiometricAuthenticator(context) }

    LaunchedEffect(key1 = true) {
        viewModel.fetchWallets()
    }

    if (!hasResponse) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        when {
            tndWallet == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            biometricAuthenticator.promptBiometricAuth(
                                title = "Authenticate",
                                subTitle = "Please authenticate to create your wallet",
                                negativeButtonText = "Cancel",
                                fragmentActivity = activity,
                                onSuccess = {
                                    showCreateDialog = true
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
                        },
                        modifier = Modifier.fillMaxWidth(0.8f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3D5AFE)
                        )
                    ) {
                        Text(
                            "Create Your First Wallet in Dinars (TND)",
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            else -> {
                Wallets(
                    wallets = currencyWallets,
                    TNDWallet = tndWallet!!,
                    onWalletSelected = { wallet -> selectedWallet = wallet },
                    selectedWallet = selectedWallet,
                    currencyConverterViewModel = currencyConverterViewModel,
                    viewModel = viewModel
                )
            }
        }
    }

    if (showCreateDialog) {
        CreateTNDWalletDialog(
            onDismiss = { showCreateDialog = false },
            onConfirm = { amount, rib ->  // Ajout du paramètre RIB
                viewModel.createTNDWallet(amount, rib.toString())
                showCreateDialog = false
            },
            viewModel = viewModel,
            hasTNDWallet = false
        )
    }
}
