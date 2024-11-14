package com.example.b_rich.ui.signin

import androidx.compose.foundation.background
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.livedata.observeAsState
import com.example.b_rich.R
import com.example.b_rich.ui.components.EmailTextField
import com.example.b_rich.ui.components.PasswordTextField
import com.example.b_rich.data.local.PreferencesManager
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import com.example.b_rich.ui.biometricDialog.BiometricAuthenticator
import com.example.b_rich.ui.theme.EMAIL
import com.example.b_rich.ui.theme.IS_REMEMBERED
import com.example.b_rich.ui.theme.PASSWORD
import com.example.b_rich.ui.theme.PREF_FILE
import com.google.gson.Gson

@Composable
fun LoginScreen(viewModel: SigninViewModel = viewModel(), navHostController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isChecked = remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    // Observe the login UI state
    val loginUiState by viewModel.loginUiState.observeAsState(LoginUiState())
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager.create(context) }
    //val mSharedPreferences = remember { context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE) }
    //biometric var
    val biometricAuthenticator= BiometricAuthenticator(context)
    val activity = LocalContext.current as FragmentActivity
    var message by  remember{
        mutableStateOf("")
    }
    var showBiometricDialog by remember { mutableStateOf(false) }


    //login standard
  LaunchedEffect(key1 = loginUiState.isLoggedIn) {
        if (loginUiState.isLoggedIn) {
            val userJson = Gson().toJson(loginUiState.user)
            navHostController.navigate("exchangeRate/$userJson") {
                // Effacer le back stack pour empêcher le retour
                popUpTo("login") { inclusive = true }
            }
        }
    }
    // Vérifier l'état "Remember me" au lancement
    // Effet de lancement pour vérifier l'authentification biométrique
   LaunchedEffect(Unit) {
        if (preferencesManager.getDataBool(IS_REMEMBERED,false)) {

            val savedEmail = preferencesManager.getData(EMAIL,"")
            val savedPassword = preferencesManager.getData(PASSWORD,"")
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
            val savedEmail =preferencesManager.getData(EMAIL,"")
            val savedPassword = preferencesManager.getData(PASSWORD,"")?: ""
            if (savedEmail.isNotEmpty() && savedPassword.isNotEmpty()) {
                // Appeler loginWithBiometric au lieu de login normal
                viewModel.loginUserWithBiometricAuth(savedEmail, savedPassword)
            }
        }
    }
    //background
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
            //txt titree
            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3D5AFE),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Hello there, sign in to continue",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            //logo
            val logo: Painter = painterResource(id = R.drawable.logo)
            Image(
                painter = logo,
                contentDescription = "Logo",
                modifier = Modifier.size(250.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            //email
            EmailTextField(
                email = email,
                onEmailChange = {
                    email = it
                    emailError = ""
                },
                emailError = emailError,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            if (emailError.isNotEmpty()) {
                Text(emailError, color = Color.Red)
            }

            Spacer(modifier = Modifier.height(8.dp))

            //pwd
            PasswordTextField(
                password = password,
                onPasswordChange = {
                    password = it
                    passwordError = "" // Reset error on input change
                },
                passwordVisible = passwordVisible,
                onTogglePasswordVisibility = { passwordVisible = !passwordVisible },
                passwordError = passwordError,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            if (passwordError.isNotEmpty()) {
                Text(passwordError, color = Color.Red)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.padding(2.dp), verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isChecked.value,
                    onCheckedChange = {
                        isChecked.value = it
                    })
                Text(text = "Remember me")
            }
            //forgetpwd
            ClickableText(
                text = AnnotatedString("Forgot your password?"),
                onClick = { },
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
            )
            Spacer(modifier = Modifier.height(16.dp))

            //sign in boutton
            Button(
                onClick = {
                    // Validate inputs
                    val isEmailValid = viewModel.validateEmail(email) { emailError = it }
                    val isPasswordValid = viewModel.validatePassword(password) { passwordError = it }

                    // Login
                    if (isEmailValid && isPasswordValid) {
                        if (isChecked.value) {
                            preferencesManager.saveData(EMAIL, email)
                            preferencesManager.saveData( PASSWORD, password)
                            preferencesManager.saveDataBool(IS_REMEMBERED, true)
                        } else {
                            preferencesManager.saveData(EMAIL, "") // Clear email
                            preferencesManager.saveData(PASSWORD, "") // Clear password
                            preferencesManager.saveDataBool(IS_REMEMBERED, false)
                        }
                        viewModel.loginUser(email, password)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
            ){
                if (loginUiState.isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text(text = "Sign In")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Error Message
            loginUiState.errorMessage?.let { errorMessage ->
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
            }
            //navigation pour page principale et detruire signin page /////TO DO
           if (loginUiState.isLoggedIn) {
             Text(text = "Login successful!", color = Color.Green)

            }
            //signup txt
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Don't have an account?", color = Color.Gray)
                Spacer(modifier = Modifier.width(4.dp))
                ClickableText(
                    text = AnnotatedString("Sign Up"),
                    onClick = { },
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF3D5AFE),
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

