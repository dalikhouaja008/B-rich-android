package com.example.b_rich

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.b_rich.data.network.RetrofitClient
import com.example.b_rich.data.repositories.UserRepository
import com.example.b_rich.ui.signin.LoginScreen
import com.example.b_rich.ui.signin.SigninViewModel
import com.example.b_rich.ui.theme.BrichTheme
import android.content.SharedPreferences
import com.example.b_rich.injection.ViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel

/*
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val apiService = RetrofitClient.getApiService()
        val userRepository = UserRepository(apiService)
        val signinViewModel = SigninViewModel(userRepository)
        setContent {
            BrichTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    LoginScreen(signinViewModel)
                }
            }
        }
    }
}
 */

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize API service and repository
        val apiService = RetrofitClient.getApiService()
        val userRepository = UserRepository(apiService)

        // Get SharedPreferences
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        /*
        // Pass SharedPreferences to SigninViewModel
        val signinViewModel = SigninViewModel(userRepository, sharedPreferences)
         */

        // Create ViewModelFactory with UserRepository and SharedPreferences
        val viewModelFactory = ViewModelFactory.getInstance(userRepository, sharedPreferences)


        // Set content for the activity
        setContent {
            BrichTheme {
                // Use the ViewModelFactory to get the SigninViewModel
                val signinViewModel: SigninViewModel = viewModel(factory = viewModelFactory)

                    // Pass the SigninViewModel to the LoginScreen
                    LoginScreen(signinViewModel)
            }
        }
    }
}
