package com.example.b_rich.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max

@Composable
fun OtpInput(
    otpLength: Int = 6,
    onOtpTextChange: (String) -> Unit,
    otp: String,
    modifier: Modifier = Modifier
) {
    val focusRequesters = remember {
        List(otpLength) { FocusRequester() }
    }
    val focusManager = LocalFocusManager.current

    // Pour gérer la suppression avec KeyEvent
    var lastKeyEvent by remember { mutableStateOf<KeyEvent?>(null) }

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier.fillMaxWidth(),
    ) {
        repeat(otpLength) { index ->
            BasicTextField(
                value = if (index < otp.length) otp[index].toString() else "",
                onValueChange = { newValue ->
                    when {
                        // Gérer la suppression
                        newValue.isEmpty() -> {
                            val updatedOtp = if (index > 0) {
                                otp.substring(0, index - 1) + otp.substring(index)
                            } else {
                                otp.substring(1)
                            }
                            onOtpTextChange(updatedOtp)
                            // Déplacer le focus vers la gauche
                            if (index > 0) {
                                focusManager.moveFocus(FocusDirection.Previous)
                            }
                        }
                        // Gérer l'ajout
                        newValue.all { it.isDigit() } -> {
                            val digit = newValue.last()
                            val updatedOtp = if (index < otp.length) {
                                otp.substring(0, index) + digit + otp.substring(index + 1)
                            } else {
                                otp + digit
                            }
                            onOtpTextChange(updatedOtp.take(otpLength))
                            // Déplacer le focus vers la droite si ce n'est pas le dernier champ
                            if (index < otpLength - 1) {
                                focusManager.moveFocus(FocusDirection.Next)
                            }
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
                        if (keyEvent.type == KeyEventType.KeyDown &&
                            keyEvent.key == Key.Backspace &&
                            otp.isNotEmpty() &&
                            index >= 0
                        ) {
                            // Supprimer le caractère actuel
                            val updatedOtp = if (index < otp.length) {
                                otp.substring(0, index) + otp.substring(index + 1)
                            } else {
                                otp.substring(0, otp.length - 1)
                            }
                            onOtpTextChange(updatedOtp)

                            // Déplacer le focus vers la gauche si ce n'est pas le premier champ
                            if (index > 0) {
                                focusManager.moveFocus(FocusDirection.Previous)
                            }
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
                    imeAction = if (index == otpLength - 1) ImeAction.Done else ImeAction.Next
                ),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        innerTextField()
                    }
                }
            )

            // Focus initial sur le premier champ
            LaunchedEffect(Unit) {
                if (index == 0) {
                    focusRequesters[0].requestFocus()
                }
            }
        }
    }
}


//pour utiliser composable otpInput ajouter dans votre code
/*
var otpValue by remember { mutableStateOf("") }

OtpInput(
    otpLength = 6,
    onOtpTextChange = { newValue -> otpValue = newValue },
    otp = otpValue
)*/