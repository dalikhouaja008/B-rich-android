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
    Scaffold(
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.White, Color(0xFFEFEFEF))
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // User Guidance
                    Text(
                        text = when (viewModel.currentStep.value) {
                            1 -> "Step 1: Enter your RIB to find your account."
                            2 -> "Step 2: Add a nickname for your account."
                            3 -> "Step 3: Enter the OTP sent to your email."
                            else -> ""
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )

                    // Step Progress Bar
                    StepProgressBar(
                        currentStep = viewModel.currentStep.value,
                        totalSteps = viewModel.totalSteps
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Render Step Content for the Current Step
                    StepView(
                        isCurrentStep = true,
                        stepNumber = viewModel.currentStep.value,
                        title = viewModel.getStepTitle(viewModel.currentStep.value),
                        content = {
                            StepContent(viewModel.currentStep.value, viewModel)
                        },
                        onStepTapped = {}
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Navigation Buttons
                    Button(
                        onClick = {
                            when (viewModel.currentStep.value) {
                                1 -> {
                                    viewModel.searchAccountByRIB(
                                        onSuccess = {
                                            viewModel.currentStep.value += 1
                                        },
                                        onFailure = {
                                            viewModel.backendMessage.value = "Account not found. Please check the RIB."
                                        }
                                    )
                                }
                                2 -> {
                                    viewModel.sendOtp(
                                        onSuccess = {
                                            viewModel.currentStep.value += 1
                                        },
                                        onFailure = {
                                            viewModel.backendMessage.value = "Failed to send OTP. Please try again."
                                        }
                                    )
                                }
                                3 -> {
                                    viewModel.verifyOtpAndSaveNickname(
                                        onSuccess = {
                                            viewModel.backendMessage.value = "Account added successfully!"
                                        },
                                        onFailure = {
                                            viewModel.backendMessage.value = "Failed to verify OTP. Please try again."
                                        }
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        enabled = viewModel.isStepValid(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (viewModel.isStepValid()) Color.Blue else Color.Gray
                        )
                    ) {
                        Text(
                            text = if (viewModel.currentStep.value < viewModel.totalSteps) "Next" else "Finish",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }

                    // Display backend messages
                    if (viewModel.backendMessage.value.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = viewModel.backendMessage.value,
                            color = if (viewModel.isBackendCallSuccessful.value) Color.Green else Color.Red,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
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
                modifier = Modifier.fillMaxWidth()
            )
        }
        2 -> {
            OutlinedTextField(
                value = viewModel.nickname.value,
                onValueChange = { viewModel.nickname.value = it },
                label = { Text("Enter Nickname") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        3 -> {
            Column {
                Text(
                    text = "Enter the 6-digit OTP sent to your email.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
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
                                if (it.length <= 1) {
                                    viewModel.otp.value[index] = it
                                }
                            },
                            modifier = Modifier
                                .width(40.dp)
                                .height(56.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
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
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        for (step in 1..totalSteps) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .background(
                        color = if (step <= currentStep) Color.Blue else Color.Gray.copy(alpha = 0.3f),
                        RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}

@Composable
fun StepView(
    isCurrentStep: Boolean,
    stepNumber: Int,
    title: String,
    content: @Composable () -> Unit,
    onStepTapped: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .background(
                if (isCurrentStep) Color(0xFFEEF4FF) else Color.Transparent,
                RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = if (isCurrentStep) Color.Blue else Color.Gray.copy(alpha = 0.3f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$stepNumber",
                    color = Color.White,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isCurrentStep) Color.Black else Color.Gray
                )

                if (isCurrentStep) {
                    Spacer(modifier = Modifier.height(8.dp))
                    content()
                }
            }
        }
    }
}
