package com.example.b_rich.ui.signin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun LoginScreen(viewModel: SigninViewModel = viewModel()) {
    // Observe the login UI state
    val loginUiState by viewModel.loginUiState.observeAsState(LoginUiState())

    // UI Elements
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Login", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Email Input
        var email by remember { mutableStateOf("") }
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password Input
        var password by remember { mutableStateOf("") }
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Login Button
        Button(
            onClick = {
                viewModel.loginUser(email, password)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Loading Indicator
        if (loginUiState.isLoading) {
            CircularProgressIndicator()
        }

        // Error Message
        loginUiState.errorMessage?.let { errorMessage ->
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }

        // Navigate to another screen if logged in
        if (loginUiState.isLoggedIn) {
            // Navigate to the next screen or show success message
            // For example:
            // navController.navigate("home")
            Text(text = "Login successful!", color = Color.Green)
            loginUiState.token?.let { Text(text = it, color = Color.Green) }
        }
    }
}
