package com.example.b_rich.ui.biometricDialog



import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.b_rich.data.network.LoginResponse
import com.example.b_rich.data.repositories.UserRepository
import com.example.b_rich.ui.signin.LoginUiState
import kotlinx.coroutines.launch
import retrofit2.Response

class biometricDialogViewModel(private val userRepository: UserRepository) : ViewModel() {

    private var _loginUiState: MutableLiveData<LoginUiState> = MutableLiveData(LoginUiState())
    val loginUiState: LiveData<LoginUiState> get() = _loginUiState // Expose as LiveData


    // Function to handle user login
    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _loginUiState.value = LoginUiState(isLoading = true)  // Set loading state

            try {
                // Make the login request
                val response: Response<LoginResponse> = userRepository.loginwithbiometric(email, password)

                if (response.isSuccessful) {
                    // recup token, id, refresh token
                    val loginResponse = response.body()
                    print(loginResponse)
                    if (loginResponse != null) {
                        val accessToken = loginResponse.accessToken
                        val refreshToken = loginResponse.refreshToken
                        val userId = loginResponse.userId
                        // Update state with success
                        _loginUiState.value = LoginUiState(isLoggedIn = true, token = accessToken, refreshToken = refreshToken, userId = userId)

                    } else {
                       //reponse est nulle
                        _loginUiState.value = LoginUiState(errorMessage = "Login failed: No response body")
                    }
                } else {
                    //log failed
                    _loginUiState.value = LoginUiState(errorMessage = "Login failed: ${response.message()}")
                }
            } catch (e: Exception) {
                //throw exception
                _loginUiState.value = LoginUiState(errorMessage = e.message)
            }
        }
    }


}