package com.example.b_rich.ui.AddAccount.componenets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.b_rich.ui.AddAccount.AddAccountViewModel
import com.example.b_rich.ui.AddAccount.components.LoadingIndicator

@Composable
fun AccountVerificationStep(viewModel: AddAccountViewModel) {
    val account by viewModel.accountDetails.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    when {
        isLoading -> LoadingIndicator()
        account != null -> AccountFoundCard(account!!)
        else -> AccountNotFoundError()
    }
}