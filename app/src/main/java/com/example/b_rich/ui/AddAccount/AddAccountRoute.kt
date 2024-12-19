package com.example.b_rich.ui.AddAccount



import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.b_rich.data.network.ApiService
import com.example.b_rich.data.network.RetrofitClient.apiService

@Composable
fun AddAccountRoute(
    viewModel: AddAccountViewModel = viewModel(
        factory = AddAccountViewModel.Factory(apiService)
    ),
    onBackToAccounts: () -> Unit
) {
    AddAccountScreen(
        viewModel = viewModel,
        onBackToAccounts = onBackToAccounts
    )
}