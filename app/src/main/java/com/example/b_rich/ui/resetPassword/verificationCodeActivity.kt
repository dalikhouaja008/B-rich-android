package com.example.b_rich.ui.resetPassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.b_rich.R
import com.example.b_rich.data.entities.user

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeEntryScreen(
    user: user, // Correction : Type User avec majuscule
    viewModel: ResetPasswordViewModel = viewModel(),
    navHostController: NavHostController
) {
    var code by remember { mutableStateOf("") }
    var codeError by remember { mutableStateOf<String?>(null) } // Nullable pour meilleure gestion
    val resetPasswordUiState by viewModel.resetPasswordUiState.observeAsState(ResetPasswordUiState())
    val focusRequesters = remember {
        List(6) { FocusRequester() }
    }
    val focusManager = LocalFocusManager.current
    // Effet pour gérer la navigation
    LaunchedEffect(resetPasswordUiState.isCodeVerified) {
        if (resetPasswordUiState.isCodeVerified) {
            navHostController.navigate("changepassword/$code/${user.email}")
        }
    }

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
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Enter Verification Code",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(250.dp)
                        .padding(vertical = 16.dp)
                )

                // OTP Input Fields avec focus management
                val focusRequester = remember { FocusRequester() }
                val focusManager = LocalFocusManager.current


                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    repeat(6) { index ->
                        BasicTextField(
                            value = if (index < code.length) code[index].toString() else "",
                            onValueChange = { newValue ->
                                if (newValue.isEmpty()) {
                                    // Gestion de la suppression
                                    if (index > 0) {
                                        // Supprimer le caractère actuel et déplacer le focus vers la gauche
                                        code = code.substring(0, index)
                                        focusManager.moveFocus(FocusDirection.Previous)
                                    } else {
                                        // Pour le premier champ, simplement supprimer
                                        code = code.substring(1)
                                    }
                                } else if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                                    // Gestion de l'ajout
                                    val newCode = if (index < code.length) {
                                        // Remplacer le caractère à la position actuelle
                                        code.substring(0, index) + newValue +
                                                code.substring(index + 1)
                                    } else {
                                        // Ajouter un nouveau caractère
                                        code + newValue
                                    }
                                    code = newCode.take(6)

                                    // Déplacer le focus vers le champ suivant si ce n'est pas le dernier
                                    if (newCode.length < 6 && index < 5) {
                                        focusManager.moveFocus(FocusDirection.Next)
                                    }
                                }
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.outline,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .focusRequester(focusRequesters[index])
                                .onKeyEvent { keyEvent ->
                                    // Gérer la touche de suppression quand le champ est vide
                                    if (keyEvent.key == Key.Backspace && index > 0 &&
                                        (index >= code.length || code[index].toString().isEmpty())) {
                                        code = code.substring(0, index - 1)
                                        focusManager.moveFocus(FocusDirection.Previous)
                                        true
                                    } else {
                                        false
                                    }
                                },
                            textStyle = TextStyle(
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = if (index == 5) ImeAction.Done else ImeAction.Next
                            ),
                            decorationBox = { innerTextField ->
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    innerTextField()
                                }
                            },
                            singleLine = true
                        )
                    }
                }
                codeError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
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
                    enabled = !resetPasswordUiState.isLoading
                ) {
                    if (resetPasswordUiState.isLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(text = "Verify Code")
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
                        onClick = { viewModel.requestReset(user.email) },
                        enabled = !resetPasswordUiState.isLoading
                    ) {
                        Text(
                            text = "Resend Code",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                resetPasswordUiState.errorMessage?.let { errorMessage ->
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                if (resetPasswordUiState.isCodeSent) {
                    Text(
                        text = "Code sent successfully. Please check your email.",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}