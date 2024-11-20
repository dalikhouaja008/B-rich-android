package com.example.b_rich.ui.AddAcount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AddAccountViewModel : ViewModel() {

    // LiveData for loading state
    private val _isAdding = MutableLiveData(false)
    val isAdding: LiveData<Boolean> get() = _isAdding

    // LiveData for error message
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    // Function to add a bank account
    fun addBankAccount(accountName: String, accountNumber: String, bankName: String) {
        if (validateInputs(accountName, accountNumber, bankName)) {
            _isAdding.value = true
            _errorMessage.value = null

            // Simulate a network or database operation
            viewModelScope.launch {
                delay(2000) // Simulated delay
                _isAdding.value = false
                _errorMessage.value = "Account added successfully!"
            }
        }
    }

    // Validate input fields
    private fun validateInputs(accountName: String, accountNumber: String, bankName: String): Boolean {
        return when {
            accountName.isBlank() -> {
                _errorMessage.value = "Account name cannot be empty."
                false
            }
            accountNumber.isBlank() -> {
                _errorMessage.value = "Account number cannot be empty."
                false
            }
            bankName.isBlank() -> {
                _errorMessage.value = "Bank name cannot be empty."
                false
            }
            accountNumber.length < 10 -> {
                _errorMessage.value = "Account number must be at least 10 digits."
                false
            }
            else -> true
        }
    }
}