package com.example.b_rich.ui.signup

import android.util.Log
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewModelScope
import com.example.b_rich.data.entities.user
import com.example.b_rich.data.repositories.UserRepository
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.launch
import retrofit2.Response

data class SignUpUiState(
    val isLoading: Boolean = false,
    val isSignedUp: Boolean = false,
    val user: user? = null,
    val errorMessage: String? = null
)

class SignupViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _signUpUiState = MutableLiveData(SignUpUiState())
    val signUpUiState: LiveData<SignUpUiState> = _signUpUiState


    fun createUser(user: user) {
        _signUpUiState.value = SignUpUiState(isLoading = true)
        viewModelScope.launch {
            try {
                Log.d("SignUpViewModel", "Creating user: $user")
                val response = userRepository.createUser(user)
                Log.d("SignUpViewModel", "Response: $response")

                when {
                    response.isSuccessful -> {
                        response.body()?.let { newUser ->
                            _signUpUiState.value = SignUpUiState(
                                isSignedUp = true,
                                user = newUser
                            )
                        }
                    }
                    else -> {
                        _signUpUiState.value = SignUpUiState(
                            errorMessage = response.errorBody()?.string() ?: "Sign up failed"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("SignUpViewModel", "Error creating user", e)
                _signUpUiState.value = SignUpUiState(
                    errorMessage = e.message ?: "An unexpected error occurred"
                )
            }
        }
    }
    fun handleSignIn(result: GetCredentialResponse) {
        // Handle the successfully returned credential.
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        // Use googleIdTokenCredential and extract id to validate and
                        // authenticate on your server.
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(credential.data)

                        // TODO: Send [googleIdTokenCredential.idToken] to your backend
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e("MainActivity", "handleSignIn:", e)
                    }
                } else {
                    // Catch any unrecognized custom credential type here.
                    Log.e("MainActivity", "Unexpected type of credential")
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.e("MainActivity", "Unexpected type of credential")
            }
        }
    }
    fun validateName(name: String, onError: (String) -> Unit): Boolean {
        return if (name.length < 3) {
            onError("Name must be at least 3 characters long")
            false
        } else {
            onError("")
            true
        }
    }

    fun validateEmail(email: String, onError: (String) -> Unit): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return if (!email.matches(emailPattern.toRegex())) {
            onError("Please enter a valid email address")
            false
        } else {
            onError("")
            true
        }
    }

    fun validatePassword(password: String, onError: (String) -> Unit): Boolean {
        return if (password.length < 6) {
            onError("Password must be at least 6 characters long")
            false
        } else {
            onError("")
            true
        }
    }

    fun validateConfirmPassword(password: String, confirmPassword: String, onError: (String) -> Unit): Boolean {
        return if (password != confirmPassword) {
            onError("Passwords do not match")
            false
        } else {
            onError("")
            true
        }
    }

}