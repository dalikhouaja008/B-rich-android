package com.example.b_rich

import ExchangeRate
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.b_rich.data.network.RetrofitClient
import com.example.b_rich.data.repositories.UserRepository
import com.example.b_rich.ui.signin.LoginScreen
import com.example.b_rich.ui.signin.SigninViewModel
import com.example.b_rich.ui.theme.BrichTheme
import com.example.b_rich.ui.theme.PREF_FILE

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val apiService = RetrofitClient.getApiService()
        val userRepository = UserRepository(apiService)
        val signinViewModel = SigninViewModel(userRepository)
        val mSharedPreferences = getSharedPreferences(PREF_FILE, MODE_PRIVATE)
        setContent {
            BrichTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {

                    val navController= rememberNavController()
                    NavHost(navController,startDestination = "loginPage") {
                        composable("loginPage"){  LoginScreen(signinViewModel,navController)}
                        composable("exchangeRate"){ExchangeRate()}
                    }

                }
            }
        }
    }
}



