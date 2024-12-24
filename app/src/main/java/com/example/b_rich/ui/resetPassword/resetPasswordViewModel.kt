package com.example.b_rich.ui.resetPassword



import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.b_rich.data.entities.user
import com.example.b_rich.data.repositories.UserRepository

import kotlinx.coroutines.launch
import javax.inject.Inject


class ResetPasswordViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _resetPasswordUiState = MutableLiveData(ResetPasswordUiState())
    val resetPasswordUiState: LiveData<ResetPasswordUiState> = _resetPasswordUiState

    fun requestReset(email: String) {
        viewModelScope.launch {
            _resetPasswordUiState.value = _resetPasswordUiState.value?.copy(isLoading = true)
            try {
                val response = userRepository.requestPasswordReset(email)
                if (response.isSuccessful) {
                    _resetPasswordUiState.value = _resetPasswordUiState.value?.copy(
                        isCodeSent = true,
                        isLoading = false
                    )
                } else {
                    _resetPasswordUiState.value = _resetPasswordUiState.value?.copy(
                        errorMessage = "Failed to send reset code",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _resetPasswordUiState.value = _resetPasswordUiState.value?.copy(
                    errorMessage = e.message,
                    isLoading = false
                )
            }
        }
    }

    fun verifyCode(email: String, code: String) {
        viewModelScope.launch {
            _resetPasswordUiState.value = _resetPasswordUiState.value?.copy(isLoading = true)
            try {
                val response = userRepository.verifyCode(email, code)
                if (response.isSuccessful) {
                    response.body()?.let { verifyResponse ->
                        if (verifyResponse.success) {
                            _resetPasswordUiState.value = _resetPasswordUiState.value?.copy(
                                isCodeVerified = true,
                                verificationCode = code,
                                isLoading = false,
                                errorMessage = null
                            )
                        } else {
                            _resetPasswordUiState.value = _resetPasswordUiState.value?.copy(
                                isCodeVerified = false,
                                isLoading = false,
                                errorMessage = verifyResponse.message
                            )
                        }
                    } ?: run {
                        _resetPasswordUiState.value = _resetPasswordUiState.value?.copy(
                            isLoading = false,
                            errorMessage = "Invalid response from server"
                        )
                    }
                } else {
                    _resetPasswordUiState.value = _resetPasswordUiState.value?.copy(
                        isLoading = false,
                        errorMessage = "Code verification failed: ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                _resetPasswordUiState.value = _resetPasswordUiState.value?.copy(
                    isLoading = false,
                    errorMessage = "Error: ${e.localizedMessage}"
                )
            }
        }
    }

    fun resetPassword(email: String, code: String, newPassword: String) {
        viewModelScope.launch {
            _resetPasswordUiState.value = _resetPasswordUiState.value?.copy(isLoading = true)
            try {
                val response = userRepository.resetPassword(email, code, newPassword)
                if (response.isSuccessful) {
                    _resetPasswordUiState.value = _resetPasswordUiState.value?.copy(
                        isPasswordReset = true,
                        isLoading = false
                    )
                } else {
                    _resetPasswordUiState.value = _resetPasswordUiState.value?.copy(
                        errorMessage = "Failed to reset password",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _resetPasswordUiState.value = _resetPasswordUiState.value?.copy(
                    errorMessage = e.message,
                    isLoading = false
                )
            }
        }
    }

    fun validateNewPassword(password: String, setError: (String) -> Unit): Boolean {
        return when {
            password.isEmpty() -> {
                setError("Password cannot be empty")
                false
            }
            password.length < 6 -> {
                setError("Password must be at least 6 characters")
                false
            }
            !password.any { it.isDigit() } -> {
                setError("Password must contain at least one number")
                false
            }
            !password.any { it.isUpperCase() } -> {
                setError("Password must contain at least one uppercase letter")
                false
            }
            else -> true
        }
    }
    fun resetState() {
        _resetPasswordUiState.value = ResetPasswordUiState()
    }
}

data class ResetPasswordUiState(
    val isLoading: Boolean = false,
    val isCodeSent: Boolean = false,
    val isCodeVerified: Boolean = false,
    val isPasswordReset: Boolean = false,
    val verificationCode: String? = null,
    val errorMessage: String? = null
)