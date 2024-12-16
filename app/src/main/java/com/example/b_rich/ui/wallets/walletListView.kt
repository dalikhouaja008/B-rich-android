package com.example.b_rich.ui.wallets

import androidx.collection.emptyLongSet
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.b_rich.data.entities.Wallet
import com.example.b_rich.data.entities.Transaction
import com.example.b_rich.ui.currency_converter.CurrencyConverterViewModel
import com.example.b_rich.ui.wallets.components.WalletCard
import com.example.b_rich.ui.wallets.components.WalletCreationBottomSheet
import com.example.b_rich.ui.wallets.components.Wallets
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WalletsScreen(
    viewModel: WalletsViewModel,
    currencyConverterViewModel: CurrencyConverterViewModel
) {
    val wallets by viewModel.wallets.collectAsState()
    val recentTransactions by viewModel.recentTransactions.collectAsState()

    // State to track the currently selected wallet
    var selectedWallet by remember { mutableStateOf<Wallet?>(null) }
<<<<<<< HEAD

    // State to control bottom sheet visibility
    var showCreateWalletBottomSheet by remember { mutableStateOf(false) }

=======
    //savoir si la reponse fetchWallets a été envoyé depuis backend afin de régler le délai
    val hasResponse by viewModel.hasResponse.collectAsState()
    val context=LocalContext.current
>>>>>>> f1034cc (corriger des bugs dans send money)
    LaunchedEffect(key1 = true) {
        viewModel.fetchWallets()
    }

<<<<<<< HEAD
    when {
        wallets.isEmpty() -> {
            // Show a button to create the first wallet in TND
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { showCreateWalletBottomSheet = true },
                    modifier = Modifier.fillMaxWidth(0.8f)
=======

    if (!hasResponse) {
        // Affichage d'un loader pendant que la requête est en cours
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        when {
            wallets.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
>>>>>>> f1034cc (corriger des bugs dans send money)
                ) {
                    Button(
                        onClick = {
                            // Navigate to wallet creation in TND
                            //viewModel.navigateToCreateWallet(currency = "TND")
                        },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Text("Create Your First Wallet in Dinars (TND)")
                    }
                }
            }

<<<<<<< HEAD
                Spacer(modifier = Modifier.height(16.dp))

                // Button to create a wallet in another currency
                Button(
                    onClick = { showCreateWalletBottomSheet = true },
                    modifier = Modifier.fillMaxWidth()
=======
            else -> {
                // Show the wallet and a button to create the first wallet in another currency
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
>>>>>>> f1034cc (corriger des bugs dans send money)
                ) {
                    // Display the single TND wallet
                    Wallets(
                        wallets = wallets,
                        recentTransactions = recentTransactions,
                        onWalletSelected = { wallet -> selectedWallet = wallet },
                        selectedWallet = selectedWallet,
                        currencyConverterViewModel = currencyConverterViewModel,
                        viewModel = viewModel
                    )

                }
            }
        }
    }

    // Bottom Sheet for Wallet Creation
    if (showCreateWalletBottomSheet) {
        WalletCreationBottomSheet(
            viewModel = viewModel,
            onDismiss = { showCreateWalletBottomSheet = false }
        )
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