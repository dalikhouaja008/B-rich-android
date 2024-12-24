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


data class ResetPasswordUiState(
    val isLoading: Boolean = false,
    val isCodeVerified: Boolean = false,
    val isCodeSent:Boolean =false,
    var isPasswordReset: Boolean = false,
    val errorMessage: String? = null,
    val hasNavigated: Boolean = false,
    val user: user? =null
)

class ResetPasswordViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {


    private var _resetPasswordUiState: MutableLiveData<ResetPasswordUiState> = MutableLiveData(ResetPasswordUiState())
    val resetPasswordUiState: LiveData<ResetPasswordUiState> get() = _resetPasswordUiState

    fun requestReset(email: String) {
        viewModelScope.launch {
            _resetPasswordUiState.value = ResetPasswordUiState(isLoading = true)

            try {
                val response = userRepository.requestPasswordReset(email)

                if (response.isSuccessful) {
                    _resetPasswordUiState.value = ResetPasswordUiState(isCodeSent = true)
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("ResetPassword", "Error: $errorBody")
                    _resetPasswordUiState.value = ResetPasswordUiState(
                        errorMessage = "Request failed: ${response.code()} - ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("ResetPassword", "Exception during request reset", e)
                _resetPasswordUiState.value = ResetPasswordUiState(
                    errorMessage = "Error: ${e.localizedMessage}"
                )
            }
        }
    }

    fun verifyCode(email: String, code: String) {
        viewModelScope.launch {
            _resetPasswordUiState.value = ResetPasswordUiState(isLoading = true)

            try {
                val response = userRepository.verifyCode(email, code)

                if (response.isSuccessful) {
                    _resetPasswordUiState.value = ResetPasswordUiState(isCodeVerified = true)
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("ResetPassword", "Error: $errorBody")
                    _resetPasswordUiState.value = ResetPasswordUiState(
                        errorMessage = "Verification failed: ${response.code()} - ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("ResetPassword", "Exception during code verification", e)
                _resetPasswordUiState.value = ResetPasswordUiState(
                    errorMessage = "Error: ${e.localizedMessage}"
                )
            }
        }
    }

    fun resetPassword(email: String, code: String, newPassword: String) {
        viewModelScope.launch {
            _resetPasswordUiState.value = ResetPasswordUiState(isLoading = true)

            try {
                val response = userRepository.resetPassword(email, code, newPassword)

                if (response.isSuccessful) {
                    _resetPasswordUiState.value = ResetPasswordUiState(isPasswordReset = true, user = response.body()?.user)
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("ResetPassword", "Error: $errorBody")
                    _resetPasswordUiState.value = ResetPasswordUiState(
                        errorMessage = "Reset failed: ${response.code()} - ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("ResetPassword", "Exception during password reset", e)
                _resetPasswordUiState.value = ResetPasswordUiState(
                    errorMessage = "Error: ${e.localizedMessage}"
                )
            }
        }
    }
    // Function to validate password
    fun validateNewPassword(password: String, setError: (String) -> Unit): Boolean {
        return when {
            password.isEmpty() -> {
                setError("New password must not be empty")
                false
            }
            password.length < 6 -> {
                setError("New password must be at least 6 characters")
                false
            }
            else -> true
        }
    }

    // Function to validate verification code
    fun validateCode(code: String, setError: (String) -> Unit): Boolean {
        return when {
            code.isEmpty() -> {
                setError("Verification code must not be empty")
                false
            }
            else -> true
        }
    }
}