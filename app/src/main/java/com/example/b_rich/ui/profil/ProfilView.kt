package com.example.b_rich.ui.profil

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.b_rich.data.entities.user
import com.example.b_rich.navigateToCodeVerification
//import com.example.b_rich.ui.resetPassword.PasswordEntryBottomSheet
import com.example.b_rich.ui.resetPassword.ResetPasswordUiState
import com.example.b_rich.ui.resetPassword.ResetPasswordViewModel
import com.example.b_rich.ui.theme.PREF_FILE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePage(
    user: user,
    viewModel: ResetPasswordViewModel = viewModel(),
) {
    // State to control the visibility of the PasswordEntryBottomSheet
    var showPasswordBottomSheet by remember { mutableStateOf(false) }
    val resetPasswordUiState by viewModel.resetPasswordUiState.observeAsState(ResetPasswordUiState())
    val isLoading = resetPasswordUiState.isLoading

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9F9F9)) // Light gray background
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // User Avatar
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF9800)), // Orange color
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user.name.firstOrNull()?.toString()?.uppercase() ?: "",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // User Information
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6D4C41) // Soft brown
                )
                Text(
                    text = "Email: ${user.email}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Phone: ${user.numTel}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            // Footer Section with Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.requestReset(user.email)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp), // Increased height
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)) // Red
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Reset Password", color = Color.White)
                    }
                }

                OutlinedButton(
                    onClick = {
                        // Add your logout logic here
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp), // Increased height
                    border = BorderStroke(1.dp, Color(0xFFF44336)), // Red border
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFF44336))
                ) {
                    Text("Logout")
                }
            }
        }

        // Show Password Entry Bottom Sheet when requestReset is successful
        /*if (showPasswordBottomSheet) {
            PasswordEntryBottomSheet(
                mail = user.email,
                viewModel = viewModel,
                onDismiss = { showPasswordBottomSheet = false }
            )
        }*/
    }

    // Effect to open PasswordEntryBottomSheet when the reset request is successful
    LaunchedEffect(resetPasswordUiState.isCodeSent) {
        if (resetPasswordUiState.isCodeSent) {
            showPasswordBottomSheet = true
        }
    }
}