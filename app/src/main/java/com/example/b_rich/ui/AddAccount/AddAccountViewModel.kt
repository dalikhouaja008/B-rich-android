package com.example.b_rich.ui.AddAccount

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class AddAccountViewModel : ViewModel() {
    val accountName = mutableStateOf("")
    val accountNumber = mutableStateOf("")
    val otp = mutableStateOf(MutableList(6) { "" })
    val currentStep = mutableStateOf(1)

    val totalSteps = 3

    fun isStepValid(): Boolean {
        return when (currentStep.value) {
            1 -> accountName.value.isNotBlank()
            2 -> accountNumber.value.isNotBlank()
            3 -> otp.value.all { it.isNotBlank() }
            else -> false
        }
    }

    fun getStepTitle(step: Int): String {
        return when (step) {
            1 -> "Account Name"
            2 -> "Account Number"
            3 -> "Enter OTP"
            else -> ""
        }
    }

    fun saveAccount() {
        println("Account Saved: Name=${accountName.value}, Number=${accountNumber.value}, OTP=${otp.value.joinToString("")}")
    }
}