package com.example.b_rich.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

@Composable
fun EmailTextField(
    email: String,
    onEmailChange: (String) -> Unit,
    emailError: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = email,
        onValueChange = {
            onEmailChange(it)
        },
        isError = emailError.isNotEmpty(),
        label = { Text(text = "Email") },
        leadingIcon = { Icon(imageVector = Icons.Outlined.Email, contentDescription = "Email Icon") },
        placeholder = { Text(text = "example@gmail.com") },
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp)
            .background(Color.White, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp)
    )
    if (emailError.isNotEmpty()) {
        Text(emailError, color = Color.Red)
    }
}

@Composable
fun PasswordTextField(
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    passwordError: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = password,
        onValueChange = {
            onPasswordChange(it)
        },
        isError = passwordError.isNotEmpty(),
        label = { Text(text = "Password") },
        leadingIcon = { Icon(imageVector = Icons.Outlined.Lock, contentDescription = "Password Icon") },
        placeholder = { Text(text = "Password") },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val visibilityIcon = if (passwordVisible) Icons.Outlined.Visibility else Icons.Filled.VisibilityOff
            IconButton(onClick = onTogglePasswordVisibility) {
                Icon(imageVector = visibilityIcon, contentDescription = "Toggle Password Visibility")
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp)
            .background(Color.White, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp)
    )
    if (passwordError.isNotEmpty()) {
        Text(passwordError, color = Color.Red)
    }
}