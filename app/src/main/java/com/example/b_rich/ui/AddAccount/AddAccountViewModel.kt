package com.example.b_rich.ui.AddAccount


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel


class AddAccountViewModel : ViewModel() {
    val accountName = mutableStateOf("")
    val accountNumber = mutableStateOf("")
    val accountType = mutableStateOf("")

    val totalSteps = 3
    val currentStep = mutableStateOf(1)

    fun getAccountTypes() = listOf("Savings", "Checking", "Credit", "Investment")

    fun saveAccount() {
        // Simulate saving the account (e.g., API call or database save)
        println("Account Saved: Name=${accountName.value}, Number=${accountNumber.value}, Type=${accountType.value}")
    }
}