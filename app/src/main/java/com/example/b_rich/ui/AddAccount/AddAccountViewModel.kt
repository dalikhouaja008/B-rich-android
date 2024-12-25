package com.example.b_rich.ui.AddAccount

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.b_rich.data.dataModel.LinkAccountRequest
import com.example.b_rich.data.dataModel.NicknameUpdateRequest
import com.example.b_rich.data.entities.CustomAccount
import com.example.b_rich.data.entities.AddAccount
import com.example.b_rich.data.network.ApiService
import com.example.b_rich.data.repositories.AccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Defines possible UI states
sealed class UiState {
    object Initial : UiState() // Ajout de l'état Initial
    object Idle : UiState()
    object Loading : UiState()
    data class Success(val message: String) : UiState()
    data class Error(val message: String) : UiState()
}
class AddAccountViewModel @Inject constructor(
    private val apiService: ApiService,
    private val accountRepository: AccountRepository // Ajout du repository
) : ViewModel() {

    private val _rib = MutableStateFlow("")
    val rib: StateFlow<String> = _rib

    private val _isRibValid = MutableStateFlow(false)
    val isRibValid: StateFlow<Boolean> = _isRibValid


    private val _isRibVerificationInProgress = MutableStateFlow(false)
    val isRibVerificationInProgress: StateFlow<Boolean> = _isRibVerificationInProgress


    private val _uiState = MutableStateFlow<UiState>(UiState.Idle) // Manages UI state
    val uiState: StateFlow<UiState> = _uiState

    private val _accountAdded = MutableStateFlow(false) // New state to track successful account addition
    val accountAdded: StateFlow<Boolean> = _accountAdded

    private val _accountDetails = MutableStateFlow<CustomAccount?>(null)
    val accountDetails: StateFlow<CustomAccount?> = _accountDetails


    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _nickname = MutableStateFlow("")
    val nickname: StateFlow<String> = _nickname.asStateFlow()

    private val _selectedAccountType = MutableStateFlow<String?>(null)
    val selectedAccountType: StateFlow<String?> = _selectedAccountType.asStateFlow()

    fun updateNickname(newNickname: String) {
        _nickname.value = newNickname
    }

    fun updateAccountType(type: String) {
        _selectedAccountType.value = type
    }
    fun validateAndUpdateRib(newRib: String) {
        _rib.value = newRib
        _uiState.value = UiState.Initial // Réinitialiser l'état UI

        if (newRib.isEmpty()) {
            _isRibValid.value = false
            return
        }

        if (isRibFormatValid(newRib)) {
            verifyRibInDatabase(newRib)
        } else {
            _isRibValid.value = false
            if (newRib.length == 20) {
                _uiState.value = UiState.Error("Invalid RIB format")
            }
        }
    }

    private fun isRibFormatValid(rib: String): Boolean {
        return rib.length == 20 && rib.all { it.isDigit() }
    }

    private fun verifyRibInDatabase(rib: String) {
        _isRibVerificationInProgress.value = true
        _uiState.value = UiState.Loading

        fetchAccountByRib(
            onSuccess = {
                _isRibVerificationInProgress.value = false
                if (isRibFormatValid(rib)) {
                    _isRibValid.value = true
                    _uiState.value = UiState.Success("RIB found! You can proceed to link this account.")
                }
            },
            onFailure = {
                _isRibValid.value = false
                _isRibVerificationInProgress.value = false
                _uiState.value = UiState.Error("This RIB does not exist in our database.")
            }
        )
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
                        _nickname.value = account.name ?: ""
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

    // Nouvelle fonction pour lier un compte
    // Modifiez la fonction linkAccount pour utiliser le repository
    fun linkAccount(onSuccess: () -> Unit) {
        if (!validateInput()) {
            _uiState.value = UiState.Error("Please fill all required fields")
            return
        }

        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                accountRepository.linkAccount(
                    rib = rib.value,
                    nickname = nickname.value.takeIf { it.isNotBlank() }
                ).fold(
                    onSuccess = { account ->
                        _accountAdded.value = true
                        _uiState.value = UiState.Success("Account linked successfully!")
                        onSuccess()
                    },
                    onFailure = { error ->
                        _uiState.value = UiState.Error(error.message ?: "Unknown error")
                    }
                )
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error: ${e.localizedMessage}")
            }
        }
    }

    private fun fetchAccountDetails(rib: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getAccountByRIB(rib)
                if (response.isSuccessful) {
                    _accountDetails.value = response.body()
                    _nickname.value = response.body()?.name ?: ""
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun isStepValid(step: Int): Boolean = when(step) {
        0 -> true // Welcome step is always valid
        1 -> _isRibValid.value
        2 -> _accountDetails.value != null
        3 -> _nickname.value.isNotBlank()
        else -> false
    }

    /**
     * Reset the state when leaving the screen
     */
    fun resetState() {
        _uiState.value = UiState.Idle
        _accountAdded.value = false
        _accountDetails.value = null
        _rib.value = ""
        _nickname.value = ""
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