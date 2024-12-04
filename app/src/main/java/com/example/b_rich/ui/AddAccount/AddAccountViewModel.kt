package com.example.b_rich.ui.AddAccount

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.b_rich.data.entities.AddAccount
import com.example.b_rich.data.network.RetrofitClient
import kotlinx.coroutines.launch

class AddAccountViewModel : ViewModel() {

    val rib = mutableStateOf("") // Step 1: RIB input
    val nickname = mutableStateOf("") // Step 2: Nickname input
    val otp = mutableStateOf(MutableList(6) { "" }) // Step 3: OTP input
    val currentStep = mutableStateOf(1) // Current step of the process
    val totalSteps = 3

    // Feedback for the user
    val backendMessage = mutableStateOf("")
    val isBackendCallSuccessful = mutableStateOf(false)

    // Step validation logic
    fun isStepValid(): Boolean {
        return when (currentStep.value) {
            1 -> rib.value.isNotBlank()
            2 -> nickname.value.isNotBlank()
            3 -> otp.value.all { it.isNotBlank() }
            else -> false
        }
    }

    fun getStepTitle(step: Int): String {
        return when (step) {
            1 -> "Enter RIB"
            2 -> "Add Nickname"
            3 -> "Enter OTP"
            else -> ""
        }
    }

    // Step 1: Search for an account using RIB
    fun searchAccountByRIB(onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.getApiService().getAccountByRIB(rib.value)

                if (response.isSuccessful) {
                    backendMessage.value = "Account found! Proceed to add a nickname."
                    isBackendCallSuccessful.value = true
                    onSuccess()
                } else {
                    backendMessage.value = "Account not found. Please check the RIB."
                    isBackendCallSuccessful.value = false
                    onFailure()
                }
            } catch (e: Exception) {
                backendMessage.value = "Error: ${e.localizedMessage}"
                isBackendCallSuccessful.value = false
                onFailure()
            }
        }
    }

    // Step 2: Send OTP after adding a nickname
    fun sendOtp(onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.getApiService().sendOtp()

                if (response.isSuccessful) {
                    backendMessage.value = "OTP sent to your email. Proceed to verify it."
                    isBackendCallSuccessful.value = true
                    onSuccess()
                } else {
                    backendMessage.value = "Failed to send OTP. Try again."
                    isBackendCallSuccessful.value = false
                    onFailure()
                }
            } catch (e: Exception) {
                backendMessage.value = "Error: ${e.localizedMessage}"
                isBackendCallSuccessful.value = false
                onFailure()
            }
        }
    }



    // Step 3: Verify the OTP and save the nickname
    fun verifyOtpAndSaveNickname(onSuccess: () -> Unit, onFailure: () -> Unit) {
        val enteredOtp = otp.value.joinToString("") // Concatenate OTP digits

        viewModelScope.launch {
            try {
                val response = RetrofitClient.getApiService().verifyAndUpdateNickname(
                    rib = rib.value,
                    otp = enteredOtp,
                    nickname = nickname.value
                )

                if (response.isSuccessful) {
                    backendMessage.value = "Nickname added successfully!"
                    isBackendCallSuccessful.value = true
                    onSuccess()
                } else {
                    backendMessage.value = "Invalid OTP or failed to add nickname."
                    isBackendCallSuccessful.value = false
                    onFailure()
                }
            } catch (e: Exception) {
                backendMessage.value = "Error: ${e.localizedMessage}"
                isBackendCallSuccessful.value = false
                onFailure()
            }
        }
    }
}
