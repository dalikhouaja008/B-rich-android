package com.example.b_rich

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.b_rich.data.dataModel.DeepLinkData
import com.example.b_rich.data.entities.user
import com.example.b_rich.data.network.RetrofitClient
import com.example.b_rich.data.repositories.AccountRepository
import com.example.b_rich.data.repositories.CurrencyConverterRepository
import com.example.b_rich.data.repositories.ExchangeRateRepository
import com.example.b_rich.data.repositories.UserRepository
import com.example.b_rich.data.repositories.WalletRepository
import com.example.b_rich.injection.ViewModelFactory
import com.example.b_rich.ui.AddAccount.AddAccountScreen
import com.example.b_rich.ui.AddAccount.AddAccountViewModel
import com.example.b_rich.ui.MainScreen
import com.example.b_rich.ui.currency_converter.CurrencyConverter
import com.example.b_rich.ui.currency_converter.CurrencyConverterViewModel
import com.example.b_rich.ui.exchange_rate.ExchangeRateViewModel
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
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Initialize your API service and repository here
        val apiService = RetrofitClient.getApiService()
        val userRepository = UserRepository(apiService)
        val exchangeRateRepository = ExchangeRateRepository(apiService)
        val currencyRepository = CurrencyConverterRepository(apiService)
        val walletRepository = WalletRepository(apiService)
        val accountRepository = AccountRepository(apiService)
        // Create ViewModelFactory with all repositories
        val viewModelFactory = ViewModelFactory.getInstance(
            userRepository,
            exchangeRateRepository,
            currencyRepository,
            walletRepository,
            accountRepository,
            apiService
        )
        // Use ViewModelProvider to create all ViewModels
        val signinViewModel: SigninViewModel =
            ViewModelProvider(this, viewModelFactory)[SigninViewModel::class.java]
        val resetPasswordViewModel: ResetPasswordViewModel =
            ViewModelProvider(this, viewModelFactory)[ResetPasswordViewModel::class.java]
        val signupViewModel: SignupViewModel =
            ViewModelProvider(this, viewModelFactory)[SignupViewModel::class.java]
        val exchangeRateViewModel: ExchangeRateViewModel =
            ViewModelProvider(this, viewModelFactory)[ExchangeRateViewModel::class.java]
        val addAccountViewModel: AddAccountViewModel =
            ViewModelProvider(this, viewModelFactory)[AddAccountViewModel::class.java]
        val currencyConverterViewModel: CurrencyConverterViewModel =
            ViewModelProvider(this, viewModelFactory)[CurrencyConverterViewModel::class.java]
        val walletsViewModel: WalletsViewModel =
            ViewModelProvider(this, viewModelFactory)[WalletsViewModel::class.java]
        val listAccountsViewModel: ListAccountsViewModel =
            ViewModelProvider(this, viewModelFactory)[ListAccountsViewModel::class.java]
        //DeepLink
        val isDeepLink = intent?.data?.let { uri ->
            uri.scheme == "myapp" && uri.host == "reset-password"
        } ?: false
        setContent {
            BrichTheme {
                //DeepLink
                val deepLinkData = remember {
                    mutableStateOf<DeepLinkData?>(null)
                }
                // Gérer le deep link
                LaunchedEffect(intent) {
                    intent?.data?.let { uri ->
                        if (uri.scheme == "myapp" && uri.host == "reset-password") {
                            val token = uri.getQueryParameter("token")
                            val email = uri.getQueryParameter("email")
                            if (token != null && email != null) {
                                deepLinkData.value = DeepLinkData(token, email)
                            }
                        }
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(
                        navController,
                        startDestination = if (isDeepLink) "loginPage" else "welcomepage"
                    ) {
                        composable("welcomepage") {
                            WelcomeScreen(signinViewModel, navController)
                        }
                        composable("signup") {
                            SignUpScreen(signupViewModel, navController)
                        }
                        composable("loginPage") {
                            LoginScreen(signinViewModel, navController, deepLinkData.value)
                        }
                        composable("addAccount") {
                            AddAccountScreen(
                                addAccountViewModel,
                                onBackToAccounts = { navController.popBackStack() })
                        }
                        composable("currencyConvert") {
                            CurrencyConverter(currencyConverterViewModel)
                        }
                        composable(
                            route = "exchangeRate/{userJson}",
                            arguments = listOf(
                                navArgument("userJson") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val userJson = backStackEntry.arguments?.getString("userJson")
                            val user = userJson?.let { Gson().fromJson(it, user::class.java) }
                            user?.let {
                                MainScreen(
                                    it,
                                    navController,
                                    resetPasswordViewModel,
                                    exchangeRateViewModel,
                                    addAccountViewModel,
                                    currencyConverterViewModel,
                                    walletsViewModel,
                                    listAccountsViewModel
                                )
                            }
                        }
                        composable(
                            route = "codeVerification/{userJson}",
                            arguments = listOf(
                                navArgument("userJson") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val userJson = backStackEntry.arguments?.getString("userJson")
                            val user = userJson?.let { Gson().fromJson(it, user::class.java) }
                            user?.let { CodeEntryScreen(it, resetPasswordViewModel, navController) }
                        }
                        composable(
                            "changepassword/{code}/{email}",
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
                    // Navigation automatique si deep link existe
                    LaunchedEffect(deepLinkData.value) {
                        if (deepLinkData.value != null && !isDeepLink) {
                            navController.navigate("loginPage") {
                                popUpTo("welcomepage") { inclusive = true }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        // Recréer l'activité pour gérer le nouveau deep link
        recreate()
    }


}
fun navigateToExchangeRate(user: user, navController: NavController) {
    val userJson = Uri.encode(Gson().toJson(user))
    navController.navigate("exchangeRate/$userJson")
}

fun navigateToCodeVerification(user: user, navController: NavController) {
    val userJson = Uri.encode(Gson().toJson(user))
    navController.navigate("codeVerification/$userJson")
}





