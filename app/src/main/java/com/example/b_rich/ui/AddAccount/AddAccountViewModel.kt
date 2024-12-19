package com.example.b_rich.ui.AddAccount

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.b_rich.data.dataModel.NicknameUpdateRequest
import com.example.b_rich.data.entities.CustomAccount
import com.example.b_rich.data.entities.AddAccount
import com.example.b_rich.data.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddAccountViewModel(private val apiService: ApiService) : ViewModel() {

    val rib = mutableStateOf("") // Holds the RIB input by the user
    val nickname = mutableStateOf("") // Holds the nickname input by the user

    private val _accountDetails = MutableStateFlow<CustomAccount?>(null) // Holds the fetched account details
    val accountDetails: StateFlow<CustomAccount?> = _accountDetails

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle) // Manages UI state
    val uiState: StateFlow<UiState> = _uiState

    private val _accountAdded = MutableStateFlow(false) // New state to track successful account addition
    val accountAdded: StateFlow<Boolean> = _accountAdded

    // Defines possible UI states
    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        data class Success(val message: String) : UiState()
        data class Error(val message: String) : UiState()
    }

    /**
     * Reset the state when leaving the screen
     */
    fun resetState() {
        _uiState.value = UiState.Idle
        _accountAdded.value = false
        _accountDetails.value = null
        rib.value = ""
        nickname.value = ""
    }

    /**
     * Fetch an account by RIB from the backend
     */
    fun fetchAccountByRib(onSuccess: () -> Unit, onFailure: () -> Unit) {
        if (rib.value.isBlank()) {
            _uiState.value = UiState.Error("RIB cannot be empty")
            onFailure()
            return
        }

        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val response = apiService.getAccountByRIB(rib.value)
                if (response.isSuccessful) {
                    response.body()?.let { account ->
                        _accountDetails.value = account
                        nickname.value = account.name ?: ""
                        _uiState.value = UiState.Success("Account fetched successfully!")
                        onSuccess()
                    } ?: run {
                        _uiState.value = UiState.Error("Account not found.")
                        onFailure()
                    }
                } else {
                    _uiState.value = UiState.Error("Failed to fetch account. Response code: ${response.code()}")
                    onFailure()
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error: ${e.localizedMessage}")
                onFailure()
            }
        }
    }

    /**
     * Add an account to the user's list after updating its nickname
     */
    fun addAccountToList(onSuccess: () -> Unit, onFailure: () -> Unit) {
        val currentAccount = _accountDetails.value ?: run {
            _uiState.value = UiState.Error("No account fetched to add.")
            onFailure()
            return
        }

        if (nickname.value.isBlank()) {
            _uiState.value = UiState.Error("Nickname cannot be empty")
            onFailure()
            return
        }

        viewModelScope.launch {
            try {
                val nicknameUpdateResponse = apiService.updateNickname(
                    rib = currentAccount.rib,
                    request = NicknameUpdateRequest(nickname = nickname.value)
                )

                if (!nicknameUpdateResponse.isSuccessful) {
                    _uiState.value = UiState.Error("Failed to update nickname. Response code: ${nicknameUpdateResponse.code()}")
                    onFailure()
                    return@launch
                }

                // Prepare the account data to be added to the user's account list
                val accountToAdd = AddAccount(
                    rib = currentAccount.rib,
                    name = nickname.value.ifBlank { currentAccount.name ?: "" },
                    balance = currentAccount.balance ?: 0.0,
                    isDefault = false
                )

                // Add the account to the user's account list
                val addAccountResponse = apiService.addAccountToUserList(accountToAdd)
                if (addAccountResponse.isSuccessful) {
                    _accountAdded.value = true
                    _uiState.value = UiState.Success("Account added successfully!")
                    onSuccess()
                } else {
                    _uiState.value = UiState.Error("Failed to add account. Response code: ${addAccountResponse.code()}")
                    onFailure()
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error: ${e.localizedMessage}")
                onFailure()
            }
        }
    }

    /**
     * Validate input before submitting
     */
    fun validateInput(): Boolean {
        return rib.value.isNotBlank() && nickname.value.isNotBlank()
    }


}