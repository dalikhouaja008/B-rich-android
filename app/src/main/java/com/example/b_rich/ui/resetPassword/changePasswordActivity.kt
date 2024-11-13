package com.example.b_rich.ui.resetPassword
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.navigation.NavHostController
@Composable
fun PasswordEntryScreen(code:String,mail:String,viewModel: ResetPasswordViewModel = viewModel(), navHostController: NavHostController) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }
    val resetPasswordUiState by viewModel.resetPasswordUiState.observeAsState(ResetPasswordUiState())

    // Background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(Color(0xFF3D5AFE), Color(0xFFB39DDB)))),
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
            // Title
            Text(
                text = "Reset Your Password",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3D5AFE),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))

            // New Password Input Field
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                isError = passwordError.isNotEmpty(),
                label = { Text(text = "New Password") },
                placeholder = { Text(text = "Enter new password") },
                visualTransformation = PasswordVisualTransformation(),
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

            // Confirm Password Input Field
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                isError = confirmPasswordError.isNotEmpty(),
                label = { Text(text = "Confirm Password") },
                placeholder = { Text(text = "Re-enter new password") },
                visualTransformation= PasswordVisualTransformation(),
                modifier= Modifier.fillMaxWidth().padding(2.dp).background(Color.White, shape= RoundedCornerShape(16.dp)),
                shape= RoundedCornerShape(16.dp)
            )
            if (confirmPasswordError.isNotEmpty()) {
                Text(confirmPasswordError, color= Color.Red)
            }

            Spacer(modifier= Modifier.height(16.dp))

            // Reset Password Button
            Button(
                onClick={
                    // Validate and proceed with resetting the password
                    val isNewPasswordValid= viewModel.validateNewPassword(newPassword) { passwordError= it }
                    val isConfirmValid= if (newPassword == confirmPassword) true else {
                        confirmPasswordError= "Passwords do not match"
                        false
                    }

                    if (isNewPasswordValid && isConfirmValid) {
                        viewModel.resetPassword(mail, code, newPassword) // Assuming email and code are available in scope.
                    }
                },
                modifier= Modifier.fillMaxWidth().height(48.dp),
                colors= ButtonDefaults.buttonColors(containerColor= Color(0xFFF44336))
            ) {
                if (resetPasswordUiState.isLoading) {
                    CircularProgressIndicator(color= MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text(text= "Reset Password")
                }
            }

            // Error Message
            resetPasswordUiState.errorMessage?.let{ errorMessage ->
                Text(text= errorMessage, color= MaterialTheme.colorScheme.error)
            }

            Spacer(modifier= Modifier.height(16.dp))

            //retourner vers page d'acceuil
            //changer pwd dans sharedpreference
            if(resetPasswordUiState.isPasswordReset){
                navHostController.navigate("exchangeRate")
            }
        }
    }


}