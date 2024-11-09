package com.example.b_rich.ui.signin

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.b_rich.R
import com.example.b_rich.ui.theme.EMAIL
import com.example.b_rich.ui.theme.IS_REMEMBERED
import com.example.b_rich.ui.theme.PASSWORD
import com.example.b_rich.ui.theme.PREF_FILE

@Composable
fun LoginScreen(viewModel: SigninViewModel = viewModel(),navHostController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isChecked = remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    val loginUiState by viewModel.loginUiState.observeAsState(LoginUiState())
    val context = LocalContext.current
    val mSharedPreferences = remember { context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE) }

    LaunchedEffect(key1 = loginUiState.isLoggedIn) {
        if (loginUiState.isLoggedIn) {
            navHostController.navigate("exchangeRate") {
                // Effacer le back stack pour empêcher le retour
                popUpTo("login") { inclusive = true }
            }
        }
    }
    // Vérifier l'état "Remember me" au lancement
    LaunchedEffect(Unit) {
        if (mSharedPreferences.getBoolean(IS_REMEMBERED, false)) {
            navHostController.navigate("exchangeRate") {
                popUpTo("login") { inclusive = true }
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
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                isError = emailError.isNotEmpty(),
                label = { Text(text = "Email") },
                leadingIcon = { Icon(imageVector = Icons.Outlined.Email, contentDescription = "Email Icon") },
                placeholder = { Text(text = "Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp)
                    .background(Color.White, shape = RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp)
            )
            if (emailError.isNotEmpty()) {
                Text(emailError, color = Color.Red)
            }

            Spacer(modifier = Modifier.height(8.dp))

            //pwd
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                isError = passwordError.isNotEmpty(),
                label = { Text(text = "Password") },
                leadingIcon = { Icon(imageVector = Icons.Outlined.Lock, contentDescription = "Password Icon") },
                placeholder = { Text(text = "Password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val visibilityIcon = if (passwordVisible) Icons.Outlined.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = visibilityIcon, contentDescription = "Toggle Password Visibility")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp)
                    .background(Color.White, shape = RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp)
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
                        print(isChecked.value)
                    })
                Text(text = "Remember me")
                Text(text = isChecked.value.toString())
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
                    //valider inputs
                    val isEmailValid = viewModel.validateEmail(email) { emailError = it }
                    val isPasswordValid = viewModel.validatePassword(password) { passwordError = it }

                    //login
                   if (isEmailValid && isPasswordValid) {


                       mSharedPreferences.edit().apply {
                           if (isChecked.value) {
                               putString(EMAIL, email)
                               putString(PASSWORD, password)
                               putBoolean(IS_REMEMBERED, true)
                           } else {
                               remove(EMAIL)
                               remove(PASSWORD)
                               putBoolean(IS_REMEMBERED, false)
                           }
                       }.apply()

                       viewModel.loginUser(email, password)
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
            ) {
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


/*@Preview(showBackground = true)
@Composable
fun PreviewSignInScreen() {
    SignInScreen()
}*/
