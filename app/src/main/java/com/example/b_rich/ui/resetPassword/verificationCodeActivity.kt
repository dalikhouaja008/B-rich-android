package com.example.b_rich.ui.resetPassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AppRegistration
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavHostController
import com.example.b_rich.R
import com.example.b_rich.data.entities.user

@Composable
fun CodeEntryScreen(user : user, viewModel: ResetPasswordViewModel = viewModel(), navHostController: NavHostController) {
    var code by remember { mutableStateOf("") }
    var codeError by remember { mutableStateOf("") }
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
                text = "Enter Verification Code",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3D5AFE),
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
            // Code Input Field
            OutlinedTextField(
                value = code,
                onValueChange = { code = it },
                isError = codeError.isNotEmpty(),
                label = { Text(text = "Verification Code") },
                placeholder = { Text(text = "Enter 6-digit code") },
                leadingIcon = { Icon(imageVector = Icons.Outlined.AppRegistration, contentDescription = "code Icon") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp)
                    .background(Color.White, shape = RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number // Set keyboard type to Number
                )
            )
            if (codeError.isNotEmpty()) {
                Text(codeError, color = Color.Red)
            }
            Spacer(modifier = Modifier.height(16.dp))
            //Text(text = user.email, color = MaterialTheme.colorScheme.error)
            // Submit Button
            Button(
                onClick = {
                    // Validate and proceed with verification
                    if (code.length == 6) {
                        viewModel.verifyCode(user.email, code)
                    } else {
                        codeError = "Code must be 6 digits"
                    }
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
            ) {

                if (resetPasswordUiState.isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text(text = "Verify Code")
                }
            }


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Don't have an account?", color = Color.Gray)
                Spacer(modifier = Modifier.width(4.dp))
                ClickableText(
                    text = AnnotatedString("Resend Code"),
                    onClick = {
                        // Trigger resend code logic here
                        viewModel.requestReset(user.email)  // Call your method to resend the code
                    },
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Blue, fontWeight = FontWeight.Bold)
                )
            }

            if (!resetPasswordUiState.isLoading) {
                Text(text = "Sending code with success. Please check your email", color = Color.Green)
            }

            // Error Message
            resetPasswordUiState.errorMessage?.let { errorMessage ->
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
            }

            if (resetPasswordUiState.isCodeVerified) {
                navHostController.navigate("changepassword/$code/${user.email}")
            }
        }
    }
}