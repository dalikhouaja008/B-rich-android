package com.example.b_rich.ui.wallets.components.QuickActionSection

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.example.b_rich.data.entities.Wallet
import com.example.b_rich.ui.biometricDialog.BiometricAuthenticator
import com.example.b_rich.ui.currency_converter.CurrencyConverterViewModel
import com.example.b_rich.ui.wallets.QuickAction
import com.example.b_rich.ui.wallets.SwapState
import com.example.b_rich.ui.wallets.WalletsViewModel
import com.example.b_rich.ui.wallets.components.dialogs.AlimentDialog
import com.example.b_rich.ui.wallets.components.dialogs.ReceiveDialog
import com.example.b_rich.ui.wallets.components.dialogs.SendFundsDialog
import com.example.b_rich.ui.wallets.components.dialogs.SwapDialog

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun QuickActionsRow(
    selectedWallet: Wallet?,
    walletsViewModel: WalletsViewModel,
    currencyConverterViewModel: CurrencyConverterViewModel,
    actions: List<QuickAction> = listOf(
        QuickAction(Icons.Default.Send, "Send"),
        QuickAction(Icons.Default.Download, "Receive"),
        QuickAction(Icons.Default.Add, "Aliment"),
        QuickAction(Icons.Default.CreditCard, "Pay"),
        QuickAction(Icons.Default.SwapVert, "Swap"), // Ajoutez l'action Swap
    )
) {
    var showAlimentDialog by remember { mutableStateOf(false) }
    var showSendDialog by remember { mutableStateOf(false) }
    var showReceiveDialog by remember { mutableStateOf(false) }
    var showSwapDialog by remember { mutableStateOf(false) }
    val uiState by currencyConverterViewModel.uiStateCurrency.collectAsState()
    val swapState by walletsViewModel.swapState.collectAsState() // Ajoutez l'état du swap

    // Ajouter le contexte et l'authentificateur biométrique
    val context = LocalContext.current
    val activity = LocalContext.current as FragmentActivity
    val biometricAuthenticator = remember { BiometricAuthenticator(context) }
    var pendingAction by remember { mutableStateOf<String?>(null) }

    // Fonction pour gérer l'authentification biométrique
    fun handleBiometricAuth(actionType: String) {
        if (selectedWallet != null) {
            biometricAuthenticator.promptBiometricAuth(
                title = "Authenticate",
                subTitle = "Please authenticate to continue",
                negativeButtonText = "Cancel",
                fragmentActivity = activity,
                onSuccess = {
                    when (actionType) {
                        "Aliment" -> showAlimentDialog = true
                        "Send" -> showSendDialog = true
                        "Receive" -> showReceiveDialog = true
                        "Swap" -> showSwapDialog = true
                    }
                },
                onFailed = {
                    // Afficher un message d'erreur
                    Toast.makeText(
                        context,
                        "Authentication failed",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onError = { _, error ->
                    // Afficher un message d'erreur
                    Toast.makeText(
                        context,
                        "Error: $error",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }
    // Observer l'état du swap
    LaunchedEffect(swapState) {
        when (swapState) {
            is SwapState.Success -> {
                val success = swapState as SwapState.Success
                Toast.makeText(context, success.message, Toast.LENGTH_LONG).show()
                if (success.status == "confirmed" || success.status == "finalized") {
                    showSwapDialog = false
                }
            }
            is SwapState.Error -> {
                Toast.makeText(
                    context,
                    (swapState as SwapState.Error).message,
                    Toast.LENGTH_LONG
                ).show()
            }
            else -> {}
        }
    }
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(actions) { action ->
            QuickActionButton(
                icon = action.icon,
                label = action.label,
                onClick = {
                    when (action.label) {
                        "Aliment", "Send", "Receive", "Swap" -> {
                            if (selectedWallet != null) {
                                handleBiometricAuth(action.label)
                            }
                        }
                    }
                }
            )
        }
    }

    if (showAlimentDialog && selectedWallet != null) {
        SwapDialog(
            wallet = selectedWallet,
            viewModel = walletsViewModel,
            onDismiss = { showSwapDialog = false },
            onSwapComplete = {
                // Rafraîchir les données si nécessaire
                walletsViewModel.fetchWallets()
            }
        )
    }

    if (showSendDialog && selectedWallet != null) {
        SendFundsDialog(
            selectedWallet = selectedWallet,
            walletsViewModel = walletsViewModel,
            onDismiss = {
                showSendDialog = false
                pendingAction = null
            }
        )
    }

    if (showReceiveDialog && selectedWallet != null) {
        ReceiveDialog(
            selectedWallet = selectedWallet,
            onDismiss = {
                showReceiveDialog = false
                pendingAction = null
            }
        )
    }

    if (showSwapDialog && selectedWallet != null) {
        SwapDialog(
            wallet = selectedWallet,
            viewModel = walletsViewModel,
            onDismiss = {
                showSwapDialog = false
                walletsViewModel.resetSwapState()
            },
            onSwapComplete = {
                // Rafraîchir les données
                walletsViewModel.fetchWallets()
            }
        )
    }

}
