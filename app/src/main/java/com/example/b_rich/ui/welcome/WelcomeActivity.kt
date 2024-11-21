package com.example.b_rich.ui.welcome

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.b_rich.R
import com.example.b_rich.ui.theme.EMAIL
import com.example.b_rich.ui.theme.IS_REMEMBERED
import com.example.b_rich.ui.theme.PASSWORD
import com.example.b_rich.ui.theme.PREF_FILE
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.b_rich.navigateToExchangeRate
import com.example.b_rich.ui.biometricDialog.BiometricAuthenticator
import com.example.b_rich.ui.signin.LoginUiState
import com.example.b_rich.ui.signin.SigninViewModel

@Composable
fun WelcomeScreen(viewModel: SigninViewModel = viewModel(), navHostController: NavHostController) {
    val context = LocalContext.current
    val loginUiState by viewModel.loginUiState.observeAsState(LoginUiState())
    val biometricAuthenticator= BiometricAuthenticator(context)
    val activity = LocalContext.current as FragmentActivity
    var message by  remember{
        mutableStateOf("")
    }
   /* var showBiometricDialog by remember { mutableStateOf(false) }
    val mSharedPreferences = remember { context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE) }
    LaunchedEffect(Unit) {
        if (mSharedPreferences.getBoolean(IS_REMEMBERED, false)) {
            val savedEmail = mSharedPreferences.getString(EMAIL, "") ?: ""
            val savedPassword = mSharedPreferences.getString(PASSWORD, "") ?: ""
            if (savedEmail.isNotEmpty() && savedPassword.isNotEmpty()) {
                showBiometricDialog = true
                // Déclencher l'authentification biométrique
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
        }
    }
    // Gérer le résultat de l'authentification biométrique
    LaunchedEffect(message) {
        if (message == "Success") {
            val savedEmail = mSharedPreferences.getString(EMAIL, "") ?: ""
            val savedPassword = mSharedPreferences.getString(PASSWORD, "") ?: ""
            if (savedEmail.isNotEmpty() && savedPassword.isNotEmpty()) {
                // Appeler loginWithBiometric au lieu de login normal
                viewModel.loginUserWithBiometricAuth(savedEmail, savedPassword)
                loginUiState.user?.let { navigateToExchangeRate(it, navHostController) }
            }
        }
    }*/
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF3D5AFE), Color(0xFFB39DDB))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.White, shape = MaterialTheme.shapes.medium)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            // Welcome Text
            Text(
                text = "Welcome to B-Rich",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3D5AFE),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your Personal Finance Manager",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            //Spacer(modifier = Modifier.height(32.dp))

            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(300.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))



            // Sign In Button
            Button(
                onClick = { navHostController.navigate("loginPage") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
            ) {
                Text(
                    text = "Sign In",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // "Have an account?" text with Sign Up link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account? ",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                ClickableText(
                    text = AnnotatedString("Sign Up"),
                    onClick = { navHostController.navigate("signup") },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF3D5AFE),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // App Description
            Text(
                text = "Start managing your finances smartly with B-Rich. Track expenses, set budgets, and achieve your financial goals.",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Preview(
    name = "Welcome Screen Preview",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun WelcomeScreenPreview() {
    val navController = rememberNavController()

    MaterialTheme {
        WelcomeScreen(navHostController = navController)
    }
}