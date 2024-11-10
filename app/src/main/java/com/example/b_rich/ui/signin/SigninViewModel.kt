package com.example.b_rich.ui.signin

import android.content.SharedPreferences
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.b_rich.data.network.LoginResponse
import com.example.b_rich.data.repositories.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Response

data class LoginUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val token: String? = null,
    val errorMessage: String? = null,
    val hasNavigated: Boolean = false
)

class SigninViewModel(
    private val userRepository: UserRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private var _loginUiState: MutableLiveData<LoginUiState> = MutableLiveData(LoginUiState())
    val loginUiState: LiveData<LoginUiState> get() = _loginUiState // Expose as LiveData

    companion object {
        const val ACCESS_TOKEN_KEY = "access_token"
        const val REFRESH_TOKEN_KEY = "refresh_token"
        const val USER_ID_KEY = "user_id"
    }

    // Function to handle user login
    fun loginUser (email: String, password: String) {
        viewModelScope.launch {
            _loginUiState.value = LoginUiState(isLoading = true)  // Set loading state

            try {
                // Make the login request
                val response: Response<LoginResponse> = userRepository.login(email, password)

                if (response.isSuccessful) {
                    // Extract tokens and user ID from the response
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        val accessToken = loginResponse.accessToken
                        val refreshToken = loginResponse.refreshToken
                        val userId = loginResponse.userId

                        // Save tokens to SharedPreferences
                        saveTokenToPreferences(ACCESS_TOKEN_KEY, accessToken)
                        saveTokenToPreferences(REFRESH_TOKEN_KEY, refreshToken)
                        saveTokenToPreferences(USER_ID_KEY, userId)

                        // Update state with success
                        _loginUiState.value = LoginUiState(isLoggedIn = true, token = accessToken)
                    } else {
                        // Handle case where response body is null
                        _loginUiState.value = LoginUiState(errorMessage = "Login failed: No response body")
                    }
                } else {
                    // Update state with error message
                    _loginUiState.value = LoginUiState(errorMessage = "Login failed: ${response.message()}")
                }
            } catch (e: Exception) {
                // Handle exceptions during the network call
                _loginUiState.value = LoginUiState(errorMessage = e.message)
            }
        }
    }

    // Save token to SharedPreferences
    private fun saveTokenToPreferences(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun validateEmail(email: String, setError: (String) -> Unit): Boolean {
        return when {
            email.isEmpty() -> {
                setError("Email must not be empty")
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                setError("Please enter a valid email")
                false
            }
            else -> true
        }
    }

    fun validatePassword(password: String, setError: (String) -> Unit): Boolean {
        return when {
            password.isEmpty() -> {
                setError("Password must not be empty")
                false
            }
            password.length < 6 -> {
                setError("Password must be at least 6 characters")
                false
            }
            else -> true
        }
    }
}