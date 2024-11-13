package com.example.b_rich

import ExchangeRate
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.b_rich.data.entities.user
import com.example.b_rich.data.network.RetrofitClient
import com.example.b_rich.data.repositories.UserRepository
import com.example.b_rich.ui.biometricDialog.BiometricAuthenticator
import com.example.b_rich.ui.resetPassword.CodeEntryScreen
import com.example.b_rich.ui.resetPassword.PasswordEntryScreen
import com.example.b_rich.ui.resetPassword.ResetPasswordViewModel
import com.example.b_rich.ui.signin.LoginScreen
import com.example.b_rich.ui.signin.SigninViewModel
import com.example.b_rich.ui.theme.BrichTheme
import com.google.gson.Gson

class MainActivity : FragmentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Assuming this is a custom function you've defined

        // Initialize your API service and repository here
        val apiService = RetrofitClient.getApiService()
        val userRepository = UserRepository(apiService)
        val signinViewModel = SigninViewModel(userRepository)
        val resetPasswordViewModel = ResetPasswordViewModel(userRepository)

        setContent {
            BrichTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    val activity = LocalContext.current as FragmentActivity
                    var message by  remember{
                        mutableStateOf("")
                    }
                    val navController = rememberNavController()

                    NavHost(navController, startDestination = "loginPage") {
                        composable("loginPage") { LoginScreen(signinViewModel, navController) }
                        composable("exchangeRate/{userJson}") {
                                backStackEntry ->
                            val userJson = backStackEntry.arguments?.getString("userJson")
                            val user = Gson().fromJson(userJson, user::class.java)
                            ExchangeRate(user,navController,resetPasswordViewModel) }
                        composable("codeVerification/{userJson}") {
                                backStackEntry ->
                            val userJson = backStackEntry.arguments?.getString("userJson")
                            val user = Gson().fromJson(userJson, user::class.java)
                            CodeEntryScreen(user,resetPasswordViewModel,navController)
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
                                PasswordEntryScreen(code, email, resetPasswordViewModel, navController)
                            }
                        }

                    }


                }
            }
        }
    }


}
/*Column(
modifier = Modifier.fillMaxSize(),
verticalArrangement = Arrangement.Center,
horizontalAlignment = Alignment.CenterHorizontally
) {
    val activity = LocalContext.current as FragmentActivity
    var message by  remember{
        mutableStateOf("")
    }
    TextButton(
        onClick = {
            biometricAuthenticator.promptBiometricAuth(
                title ="login",
                subTitle ="Use your finger print or face id",
                negativeButtonText ="Cancel",
                fragmentActivity = activity,
                onSuccess = {
                    message="Success"
                },
                onFailed = {
                    message="Wrong fingerprint or face id"
                },
                onError = { _, error->
                    message= error.toString()

                }
            )
        }
    ) {
        Text(text = "Login with fingerprint or face id")
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = message)
    }

}*/
/*Scaffold(modifier = Modifier.fillMaxSize()) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "loginPage") {
        composable("loginPage") { LoginScreen(signinViewModel, navController) }
        composable("exchangeRate") { ExchangeRate() }
    }
}*/



/*  private val promptManager by lazy {
      BiometricPromptManager(this)
  }*/

/*composable("loginPage") {
                           // Utilisez viewModel() pour obtenir les ViewModels
                           val signinViewModel: SigninViewModel = viewModel()
                           val biometricViewModel: biometricDialogViewModel = viewModel()

                           LoginScreen(
                               viewModel = signinViewModel,
                               navHostController = navController,
                               biometricPrompt = promptManager,
                               biometricViewModel = biometricViewModel
                           )
                       }*/

