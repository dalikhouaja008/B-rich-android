package com.example.b_rich.ui.resetPassword

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import com.example.b_rich.ui.theme.PASSWORD
import com.example.b_rich.ui.theme.PREF_FILE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordEntryBottomSheet(
    mail: String,
    viewModel: ResetPasswordViewModel = viewModel(),
    onDismiss: () -> Unit
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    val resetPasswordUiState by viewModel.resetPasswordUiState.observeAsState(ResetPasswordUiState())
    val context = LocalContext.current
    val sharedPreferences = remember { context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE) }

    // Gérer le succès de la réinitialisation
    LaunchedEffect(resetPasswordUiState.isPasswordReset) {
        if (resetPasswordUiState.isPasswordReset) {
            sharedPreferences.edit().apply {
                putString(PASSWORD, newPassword)
                apply()
            }
            showSuccessDialog = true
        }
    }

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Reset Your Password",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = {
                    newPassword = it
                    passwordError = ""
                },
                isError = passwordError.isNotEmpty(),
                label = { Text("New Password") },
                placeholder = { Text("Enter new password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = { Icon(Icons.Outlined.Lock, "Password Icon") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            "Toggle Password Visibility"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            if (passwordError.isNotEmpty()) {
                Text(
                    text = passwordError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordError = ""
                },
                isError = confirmPasswordError.isNotEmpty(),
                label = { Text("Confirm Password") },
                placeholder = { Text("Re-enter new password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = { Icon(Icons.Outlined.Lock, "Password Icon") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            "Toggle Password Visibility"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            if (confirmPasswordError.isNotEmpty()) {
                Text(
                    text = confirmPasswordError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val isNewPasswordValid = viewModel.validateNewPassword(newPassword) {
                        passwordError = it
                    }
                    val isConfirmValid = if (newPassword == confirmPassword) true else {
                        confirmPasswordError = "Passwords do not match"
                        false
                    }

                    if (isNewPasswordValid && isConfirmValid) {
                        viewModel.resetPassword(mail, resetPasswordUiState.verificationCode!!, newPassword)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !resetPasswordUiState.isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                if (resetPasswordUiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text("Reset Password")
                }
            }
        }
    }
    // Dialog de succès
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                onDismiss()
            },
            title = { Text("Success") },
            text = { Text("Your password has been successfully reset.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSuccessDialog = false
                        onDismiss()
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

