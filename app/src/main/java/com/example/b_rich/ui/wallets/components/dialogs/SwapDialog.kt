package com.example.b_rich.ui.wallets.components.dialogs

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.outlined.Money
import androidx.compose.material.icons.outlined.Percent
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.b_rich.data.dataModel.SwapRequest
import com.example.b_rich.data.entities.Wallet
import com.example.b_rich.ui.wallets.SwapState
import com.example.b_rich.ui.wallets.WalletsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwapDialog(
    wallet: Wallet,
    viewModel: WalletsViewModel,
    onDismiss: () -> Unit,
    onSwapComplete: () -> Unit
) {
    var selectedToSymbol by remember { mutableStateOf<String?>(null) }
    var amount by remember { mutableStateOf("") }
    var slippage by remember { mutableStateOf("1.0") }

    val availableTokens by viewModel.availableTokens.collectAsState()
    val swapState by viewModel.swapState.collectAsState()
    var expandedDropdown by remember { mutableStateOf(false) }
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.loadAvailableTokens()
    }
    // Surveiller l'état du swap
    LaunchedEffect(swapState) {
        when (swapState) {
            is SwapState.Success -> {
                Toast.makeText(context, (swapState as SwapState.Success).message, Toast.LENGTH_LONG).show()
                onSwapComplete()
                onDismiss()
            }
            is SwapState.Error -> {
                Toast.makeText(context, (swapState as SwapState.Error).message, Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    AlertDialog(
        onDismissRequest = {
            if (swapState !is SwapState.Loading) {
                onDismiss()
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.SwapVert,
                    contentDescription = "Swap Icon",
                    tint = Color(0xFF3D5AFE)
                )
                Text(
                    text = "Swap ${wallet.currency}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF3D5AFE)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // From Currency
                OutlinedTextField(
                    value = "SOL",
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("From") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Money,
                            contentDescription = "From Currency"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp)
                )

                // To Currency Dropdown
                ExposedDropdownMenuBox(
                    expanded = expandedDropdown,
                    onExpandedChange = { expandedDropdown = it }
                ) {
                    OutlinedTextField(
                        value = selectedToSymbol ?: "Select Token",
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("To") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.SwapVert,
                                contentDescription = "To Currency"
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .background(Color.White, shape = RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = expandedDropdown,
                        onDismissRequest = { expandedDropdown = false }
                    ) {
                        availableTokens
                            .filter { it.symbol != wallet.currency }
                            .forEach { token ->
                                DropdownMenuItem(
                                    text = { Text("${token.symbol}") },
                                    onClick = {
                                        selectedToSymbol = token.symbol
                                        expandedDropdown = false
                                    }
                                )
                            }
                    }
                }

                // Amount Input
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Money,
                            contentDescription = "Amount"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp)
                )

                // Slippage Input
                OutlinedTextField(
                    value = slippage,
                    onValueChange = { slippage = it },
                    label = { Text("Slippage (%)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Percent,
                            contentDescription = "Slippage"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp)
                )

                if (swapState is SwapState.Loading) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF3D5AFE)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amountDouble = amount.toDoubleOrNull()
                    val slippageDouble = slippage.toDoubleOrNull()?.div(100)

                    if (selectedToSymbol != null && amountDouble != null && slippageDouble != null) {
                        viewModel.executeSwap(
                            SwapRequest(
                                fromSymbol = "SOL",
                                toSymbol = selectedToSymbol!!,
                                amount = amountDouble,
                                slippage = slippageDouble,
                                userPublicKey = wallet.publicKey ?: ""
                            )
                        )
                    }
                },
                enabled = selectedToSymbol != null &&
                        amount.isNotEmpty() &&
                        slippage.isNotEmpty() &&
                        swapState !is SwapState.Loading,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3D5AFE))
            ) {
                if (swapState is SwapState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White
                    )
                } else {
                    Text("Swap", color = Color.White)
                }
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                enabled = swapState !is SwapState.Loading,
                border = BorderStroke(1.dp, Color(0xFF3D5AFE)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF3D5AFE))
            ) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}