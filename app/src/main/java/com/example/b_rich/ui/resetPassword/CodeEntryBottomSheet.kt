package com.example.b_rich.ui.resetPassword

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.b_rich.data.entities.user
import com.example.b_rich.ui.components.OtpInput
import com.example.b_rich.ui.signin.SigninViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeEntryBottomSheet(
    user: user,
    viewModel: ResetPasswordViewModel = viewModel(),
    onDismiss: () -> Unit,
    onVerificationSuccess: () -> Unit
) {
    var code by remember { mutableStateOf("") }
    var codeError by remember { mutableStateOf<String?>(null) }
    val resetPasswordUiState by viewModel.resetPasswordUiState.observeAsState(ResetPasswordUiState())
    var showAlert by remember { mutableStateOf(false) }

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    // Gérer la vérification réussie
    LaunchedEffect(resetPasswordUiState.isCodeVerified) {
        if (resetPasswordUiState.isCodeVerified) {
            showAlert = true
        }
    }

    if (showAlert) {
        AlertDialog(
            onDismissRequest = {
                showAlert = false
                if (resetPasswordUiState.isCodeVerified) {
                    onVerificationSuccess()
                }
            },
            title = { Text(if (resetPasswordUiState.isCodeVerified) "Success" else "Error") },
            text = {
                Text(
                    resetPasswordUiState.errorMessage
                        ?: "Code verified successfully. You can now reset your password."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showAlert = false
                        if (resetPasswordUiState.isCodeVerified) {
                            onVerificationSuccess()
                        }
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

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
                text = "Enter Verification Code",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Please enter the verification code sent to your email",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // OTP Input
            OtpInput(
                otpLength = 6,
                onOtpTextChange = { newValue ->
                    code = newValue
                    codeError = null
                },
                otp = code,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            if (codeError != null) {
                Text(
                    text = codeError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    when {
                        code.isEmpty() -> codeError = "Please enter the verification code"
                        code.length != 6 -> codeError = "Code must be 6 digits"
                        else -> {
                            codeError = null
                            viewModel.verifyCode(user.email, code)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = !resetPasswordUiState.isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                if (resetPasswordUiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text("Verify Code")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Didn't receive the code?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(
                    onClick = {
                        viewModel.requestReset(user.email)
                        code = "" // Réinitialiser le code
                    },
                    enabled = !resetPasswordUiState.isLoading
                ) {
                    Text(
                        text = "Resend Code",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            if (resetPasswordUiState.isCodeSent) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Code sent successfully. Please check your email.",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            resetPasswordUiState.errorMessage?.let { errorMessage ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Ajouter un espace en bas pour éviter que le contenu ne soit coupé
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}