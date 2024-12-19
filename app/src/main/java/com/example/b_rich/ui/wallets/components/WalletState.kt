package com.example.b_rich.ui.wallets.components

import android.content.Context
import android.widget.Toast
import com.example.b_rich.data.entities.Wallet

sealed class CreateWalletState {
    object Idle : CreateWalletState()
    object Loading : CreateWalletState()
    data class Success(val wallet: Wallet) : CreateWalletState()
    data class Error(val message: String) : CreateWalletState()
}

sealed class SendTransactionState {
    object Idle : SendTransactionState()
    object Loading : SendTransactionState()
    data class Success(val signature: String? = null) : SendTransactionState()
    data class Error(val message: String) : SendTransactionState()
}

// Extension functions pour les notifications
fun CreateWalletState.showNotification(context: Context) {
    when (this) {
        is CreateWalletState.Success -> {
            Toast.makeText(
                context,
                "Wallet created successfully with balance: ${wallet.originalAmount} TND",
                Toast.LENGTH_SHORT
            ).show()
        }
        is CreateWalletState.Error -> {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
        else -> {}
    }
}

