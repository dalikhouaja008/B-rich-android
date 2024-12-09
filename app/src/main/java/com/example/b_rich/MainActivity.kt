package com.example.b_rich

import ListAccountsViewModel
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.b_rich.data.entities.user
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
import com.example.b_rich.ui.listAccounts.ListAccountsView
import com.example.b_rich.ui.resetPassword.CodeEntryScreen
import com.example.b_rich.ui.resetPassword.PasswordEntryScreen
//import com.example.b_rich.ui.resetPassword.PasswordEntryScreen
import com.example.b_rich.ui.resetPassword.ResetPasswordViewModel
import com.example.b_rich.ui.signin.LoginScreen
import com.example.b_rich.ui.signin.SigninViewModel
import com.example.b_rich.ui.signup.SignUpScreen
import com.example.b_rich.ui.signup.SignupViewModel
import com.example.b_rich.ui.theme.BrichTheme
import com.example.b_rich.ui.welcome.WelcomeScreen
import com.example.b_rich.ui.wallets.WalletsViewModel
import com.google.gson.Gson

class MainActivity : FragmentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize API services and repositories
        val apiService = RetrofitClient.getApiService()
        val userRepository = UserRepository(apiService)
        val exchangeRateRepository = ExchangeRateRepository(apiService)
        val currencyRepository = CurrencyConverterRepository(apiService)

        // Initialize ViewModels
        val signinViewModel = SigninViewModel(userRepository)
        val resetPasswordViewModel = ResetPasswordViewModel(userRepository)
        val signupViewModel = SignupViewModel(userRepository)
        val exchangeRateViewModel = ExchangeRateViewModel(exchangeRateRepository)
        val addAccountViewModel = AddAccountViewModel()
        val currencyConverterViewModel = CurrencyConverterViewModel(currencyRepository)
        val walletsViewModel = WalletsViewModel()

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
                            AddAccountScreen(viewModel = addAccountViewModel)
                        }

                        // List Accounts Screen
                        composable("listAccounts") {
                            val listAccountsViewModel: ListAccountsViewModel = viewModel()
                            ListAccountsView(
                                viewModel = listAccountsViewModel,
                                onAddAccountClick = { navController.navigate("addAccount") }
                            )
                        }

                        // Exchange Rate Screen with User
                        composable(
                            route = "exchangeRate/{userJson}",
                            arguments = listOf(navArgument("userJson") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val userJson = backStackEntry.arguments?.getString("userJson")
                            val user = userJson?.let { Gson().fromJson(it, user::class.java) }
                            user?.let {
                                MainScreen(
                                    it,
                                    navController,
                                    resetPasswordViewModel,
                                    exchangeRateViewModel,
                                    viewModel(),
                                    addAccountViewModel,
                                    currencyConverterViewModel,
                                    walletsViewModel
                                )
                            }
                        }

                        // Code Verification Screen
                        composable(
                            route = "codeVerification/{userJson}",
                            arguments = listOf(navArgument("userJson") { type = NavType.StringType })
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

    // Utility function to navigate to Exchange Rate
    fun navigateToExchangeRate(user: user, navController: NavController) {
        val userJson = Uri.encode(Gson().toJson(user))
        navController.navigate("exchangeRate/$userJson")
    }

    // Utility function to navigate to Code Verification
    fun navigateToCodeVerification(user: user, navController: NavController) {
        val userJson = Uri.encode(Gson().toJson(user))
        navController.navigate("codeVerification/$userJson")
    }
}
