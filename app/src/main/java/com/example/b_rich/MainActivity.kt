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


