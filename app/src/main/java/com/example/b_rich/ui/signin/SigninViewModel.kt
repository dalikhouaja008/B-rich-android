package com.example.b_rich.ui.signin

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.b_rich.data.entities.user
import com.example.b_rich.data.network.AuthInterceptor
import com.example.b_rich.data.network.LoginResponse
import com.example.b_rich.data.repositories.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Response


data class LoginUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val token: String? = null,
    val refreshToken :String? =null,
    val user: user? =null,
    val errorMessage: String? = null,
    val hasNavigated: Boolean = false
)

class SigninViewModel(private val userRepository: UserRepository) : ViewModel() {

    private var _loginUiState: MutableLiveData<LoginUiState> = MutableLiveData(LoginUiState())
    val loginUiState: LiveData<LoginUiState> get() = _loginUiState // Expose as LiveData


    // Function to handle user login
    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _loginUiState.value = LoginUiState(isLoading = true)  // Set loading state

            try {
                // Make the login request
                Log.d("LoginRequest", "Email: $email, Password: $password")
                val response: Response<LoginResponse> = userRepository.login(email, password)
                Log.d("LoginResponse", "Response Code: ${response.code()}, Message: ${response.message()}")

                if (response.isSuccessful) {
                    // Extract tokens and user ID from the response
                    val loginResponse = response.body()
                    loginResponse?.let {
                        val accessToken = it.accessToken
                        val refreshToken = it.refreshToken
                        val user = it.user

                        // Stocker le token dans l'intercepteur
                        AuthInterceptor.setToken(accessToken)

                        // Sauvegarder les tokens de manière sécurisée (optionnel)
                        //saveTokensSecurely(accessToken, refreshToken)

                        // Update state with success
                        _loginUiState.value = LoginUiState(
                            isLoggedIn = true,
                            token = accessToken,
                            refreshToken = refreshToken,
                            user = user
                        )
                    } ?: run {
                        // Handle case where response body is null
                        _loginUiState.value = LoginUiState(errorMessage = "Login failed: No response body")
                    }
                } else {
                    // Update state with error message
                    _loginUiState.value = LoginUiState(errorMessage = "Login failed: ${response.message()}")
                }
            } catch (e: Exception) {
                // Handle exceptions during the network call
                _loginUiState.value = LoginUiState(errorMessage = e.localizedMessage ?: "Login failed")
            }
        }
    }

    fun loginUserWithBiometricAuth(email: String, password: String) {
        viewModelScope.launch {
            _loginUiState.value = LoginUiState(isLoading = true)  // Set loading state

            try {
                // Make the login request
                val response: Response<LoginResponse> = userRepository.loginWithBiometric(email, password)

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    loginResponse?.let {
                        val accessToken = it.accessToken
                        val refreshToken = it.refreshToken
                        val user = it.user

                        // Stocker le token dans l'intercepteur
                        AuthInterceptor.setToken(accessToken)
                       // Log.d("token $accessToken")
                        // Sauvegarder les tokens de manière sécurisée (optionnel)
                        //saveTokensSecurely(accessToken, refreshToken)

                        // Update state with success
                        _loginUiState.value = LoginUiState(
                            isLoggedIn = true,
                            token = accessToken,
                            refreshToken = refreshToken,
                            user = user
                        )
                    } ?: run {
                        // reponse est nulle
                        _loginUiState.value = LoginUiState(errorMessage = "Login failed: No response body")
                    }

                } else {
                    // log failed
                    _loginUiState.value = LoginUiState(errorMessage = "Login failed: ${response.message()}")
                }
            } catch (e: Exception) {
                // throw exception
                _loginUiState.value = LoginUiState(errorMessage = e.localizedMessage ?: "Login failed")
            }
        }
    }
    // Fonction pour sauvegarder les tokens de manière sécurisée
    /*  private fun saveTokensSecurely(accessToken: String, refreshToken: String) {
          // Utiliser EncryptedSharedPreferences ou un gestionnaire de clés sécurisé
          val sharedPreferences = EncryptedSharedPreferences.create(
              "secure_tokens",
              masterKey,
              context,
              EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
              EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
          )

          sharedPreferences.edit()
              .putString("access_token", accessToken)
              .putString("refresh_token", refreshToken)
              .apply()
      }*/

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

    /* fun logout() {
       // Réinitialiser le token dans l'intercepteur
       AuthInterceptor.setToken(null)

       // Effacer les tokens stockés
       val sharedPreferences = EncryptedSharedPreferences.create(
           "secure_tokens",
           masterKey,
           context,
           EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
           EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
       )

       sharedPreferences.edit().clear().apply()

       // Réinitialiser l'état de connexion
       _loginUiState.value = LoginUiState()
   }*/
}