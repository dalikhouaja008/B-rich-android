package com.example.b_rich.ui.AddAccount

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccountScreen(viewModel: AddAccountViewModel = remember { AddAccountViewModel() }) {
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.primary)
    )

    Scaffold(
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(gradientBackground)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Step Header
                    Column {
                        Text(
                            text = viewModel.getStepTitle(viewModel.currentStep.value),
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        )

                        StepProgressBar(
                            currentStep = viewModel.currentStep.value,
                            totalSteps = viewModel.totalSteps
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Step Content
                    StepContent(step = viewModel.currentStep.value, viewModel = viewModel)

                    Spacer(modifier = Modifier.height(24.dp))

                    // Navigation Buttons
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (viewModel.currentStep.value > 1) {
                                Button(
                                    onClick = { viewModel.currentStep.value -= 1 },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                                    ),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "Back",
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = {
                                    when (viewModel.currentStep.value) {
                                        1 -> viewModel.searchAccountByRIB(
                                            onSuccess = { viewModel.currentStep.value += 1 },
                                            onFailure = { viewModel.backendMessage.value = "RIB not found." }
                                        )
                                        2 -> viewModel.sendOtp(
                                            onSuccess = { viewModel.currentStep.value += 1 },
                                            onFailure = { viewModel.backendMessage.value = "Failed to send OTP." }
                                        )
                                        3 -> viewModel.verifyOtpAndSaveNickname(
                                            onSuccess = { viewModel.backendMessage.value = "Account added successfully!" },
                                            onFailure = { viewModel.backendMessage.value = "Failed to verify OTP." }
                                        )
                                    }
                                },
                                enabled = viewModel.isStepValid(),
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text(
                                    text = if (viewModel.currentStep.value < viewModel.totalSteps) "Next" else "Finish",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Backend Message
                        if (viewModel.backendMessage.value.isNotBlank()) {
                            Text(
                                text = viewModel.backendMessage.value,
                                color = if (viewModel.isBackendCallSuccessful.value) Color.Green else Color.Red,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp) // Added bottom padding
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun StepContent(step: Int, viewModel: AddAccountViewModel) {
    when (step) {
        1 -> {
            OutlinedTextField(
                value = viewModel.rib.value,
                onValueChange = { viewModel.rib.value = it },
                label = { Text("Enter RIB") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )
        }
        2 -> {
            OutlinedTextField(
                value = viewModel.nickname.value,
                onValueChange = { viewModel.nickname.value = it },
                label = { Text("Enter Nickname") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )
        }
        3 -> {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Enter the 6-digit OTP sent to your email.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (index in 0..5) {
                        OutlinedTextField(
                            value = viewModel.otp.value[index],
                            onValueChange = {
                                if (it.length <= 1) viewModel.otp.value[index] = it
                            },
                            modifier = Modifier
                                .width(40.dp)
                                .height(56.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StepProgressBar(currentStep: Int, totalSteps: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (step in 1..totalSteps) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .background(
                        color = if (step <= currentStep) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                        RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddAccountScreen() {
    AddAccountScreen()
}
