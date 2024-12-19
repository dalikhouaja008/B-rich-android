package com.example.b_rich


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.b_rich.data.entities.user
import com.example.b_rich.data.network.ApiService
import com.example.b_rich.data.network.RetrofitClient
import com.example.b_rich.data.repositories.CurrencyConverterRepository
import com.example.b_rich.data.repositories.ExchangeRateRepository
import com.example.b_rich.data.repositories.UserRepository
import com.example.b_rich.ui.AddAccount.AddAccountScreen
import com.example.b_rich.ui.AddAccount.AddAccountViewModel
import com.example.b_rich.ui.MainScreen
import com.example.b_rich.ui.currency_converter.CurrencyConverter
import com.example.b_rich.ui.currency_converter.CurrencyConverterViewModel
import com.example.b_rich.ui.exchange_rate.ExchangeRateViewModel
import com.example.b_rich.ui.listAccounts.ListAccountsScreen
import com.example.b_rich.ui.listAccounts.ListAccountsViewModel
import com.example.b_rich.ui.resetPassword.CodeEntryScreen
import com.example.b_rich.ui.resetPassword.PasswordEntryScreen
import com.example.b_rich.ui.resetPassword.ResetPasswordViewModel
import com.example.b_rich.ui.signin.LoginScreen
import com.example.b_rich.ui.signin.SigninViewModel
import com.example.b_rich.ui.signup.SignUpScreen
import com.example.b_rich.ui.signup.SignupViewModel
import com.example.b_rich.ui.theme.BrichTheme
import com.example.b_rich.ui.wallets.WalletsViewModel
import com.example.b_rich.ui.welcome.WelcomeScreen
import com.google.gson.Gson

class MainActivity : FragmentActivity() {

    private lateinit var apiService: ApiService

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize API services and repositories
        apiService = RetrofitClient.apiService
        val userRepository = UserRepository(apiService)
        val exchangeRateRepository = ExchangeRateRepository(apiService)
        val currencyRepository = CurrencyConverterRepository(apiService)

        // Initialize ViewModels
        val addAccountViewModel = AddAccountViewModel(apiService)
        val signinViewModel = SigninViewModel(userRepository)
        val resetPasswordViewModel = ResetPasswordViewModel(userRepository)
        val signupViewModel = SignupViewModel(userRepository)
        val exchangeRateViewModel = ExchangeRateViewModel(exchangeRateRepository)
        val currencyConverterViewModel = CurrencyConverterViewModel(currencyRepository)
        val walletsViewModel = WalletsViewModel()
        val listAccountsViewModel = ListAccountsViewModel(apiService)

        setContent {
            BrichTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    NavHost(navController, startDestination = "welcomepage") {

                        // Welcome Page
                        composable("welcomepage") {
                            WelcomeScreen(signinViewModel, navController)
                        }

                        // Signup Page
                        composable("signup") {
                            SignUpScreen(signupViewModel, navController)
                        }

                        // Login Page
                        composable("loginPage") {
                            LoginScreen(signinViewModel, navController)
                        }

                        // Currency Converter
                        composable("currencyConvert") {
                            CurrencyConverter(currencyConverterViewModel)
                        }

                        // Add Account Screen
                        composable("addAccount") {
                            AddAccountScreen(
                                viewModel = addAccountViewModel,
                                onBackToAccounts = { navController.navigate("listAccounts") }
                            )
                        }

                        // List Accounts Screen
                        composable("listAccounts") {
                            ListAccountsScreen(
                                viewModel = listAccountsViewModel,
                                onAddAccountClick = { navController.navigate("addAccount") }
                            )
                        }
                        // Exchange Rate Screen with User
                        composable(
                            route = "exchangeRate/{userJson}",
                            arguments = listOf(navArgument("userJson") {
                                type = NavType.StringType
                            })
                        ) { backStackEntry ->
                            val userJson = backStackEntry.arguments?.getString("userJson")
                            val user = userJson?.let { Gson().fromJson(it, user::class.java) }
                            user?.let {
                                MainScreen(
                                    user = it,
                                    navHostController = navController,
                                    viewModel = resetPasswordViewModel,
                                    exchangeRateViewModel = exchangeRateViewModel,
                                    addAccountViewModel = addAccountViewModel,
                                    currencyConverterViewModel = currencyConverterViewModel,
                                    walletsViewModel = walletsViewModel,
                                    listAccountsViewModel = listAccountsViewModel // Added this parameter
                                )
                            }
                        }

                        // Code Verification Screen
                        composable(
                            route = "codeVerification/{userJson}",
                            arguments = listOf(navArgument("userJson") {
                                type = NavType.StringType
                            })
                        ) { backStackEntry ->
                            val userJson = backStackEntry.arguments?.getString("userJson")
                            val user = userJson?.let { Gson().fromJson(it, user::class.java) }
                            user?.let {
                                CodeEntryScreen(it, resetPasswordViewModel, navController)
                            }
                        }

                        // Password Change Screen
                        composable(
                            route = "changepassword/{code}/{email}",
                            arguments = listOf(
                                navArgument("code") { type = NavType.StringType },
                                navArgument("email") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val code = backStackEntry.arguments?.getString("code")
                            val email = backStackEntry.arguments?.getString("email")
                            if (code != null && email != null) {
                                PasswordEntryScreen(
                                    code,
                                    email,
                                    resetPasswordViewModel,
                                    navController
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}