package com.example.b_rich.ui.AddAccount

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import com.example.b_rich.ui.AddAccount.componenets.AccountTextField
import com.example.b_rich.ui.AddAccount.componenets.AddAccountTopBar
import com.example.b_rich.ui.AddAccount.componenets.GradientButton
import com.example.b_rich.ui.AddAccount.componenets.ProgressIndicator
import com.example.b_rich.ui.AddAccount.componenets.StepCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccountScreen(
    viewModel: AddAccountViewModel,
    onBackToAccounts: () -> Unit
) {
    // State and constants
    val steps = 2
    var currentStep by remember { mutableStateOf(1) }
    val scrollState = rememberScrollState()

    // Theme colors
    val gradientColors = listOf(
        Color(0xFF6200EE),
        Color(0xFF9C27B0),
        Color(0xFF3700B3)
    )

    Scaffold(
        topBar = {
            AddAccountTopBar(onBackToAccounts = onBackToAccounts)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            // Progress indicator
            ProgressIndicator(
                currentStep = currentStep,
                totalSteps = steps,
                gradientColors = gradientColors
            )

            // Main content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp)
            ) {
                // Step 1: RIB Number
                StepCard(
                    stepNumber = 1,
                    isCurrentStep = currentStep == 1,
                    title = "RIB Number",
                    subtitle = "Enter your bank identifier",
                    onClick = { currentStep = 1 }
                ) {
                    if (currentStep == 1) {
                        AccountTextField(
                            value = viewModel.rib.value,
                            onValueChange = { viewModel.rib.value = it },
                            placeholder = "Enter your RIB",
                            gradientColor = gradientColors[0]
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Step 2: Account Name
                StepCard(
                    stepNumber = 2,
                    isCurrentStep = currentStep == 2,
                    title = "Account Name",
                    subtitle = "Personalize your account",
                    onClick = { currentStep = 2 }
                ) {
                    if (currentStep == 2) {
                        AccountTextField(
                            value = viewModel.nickname.value,
                            onValueChange = { viewModel.nickname.value = it },
                            placeholder = "Choose a name for this account",
                            gradientColor = gradientColors[0]
                        )
                    }
                }
            }
            // Bottom navigation button
            GradientButton(
                text = if (currentStep < steps) "Next" else "Finish",
                showArrow = currentStep < steps,
                enabled = when (currentStep) {
                    1 -> viewModel.rib.value.isNotEmpty()
                    2 -> viewModel.nickname.value.isNotEmpty()
                    else -> false
                },
                gradientColors = gradientColors,
                onClick = {
                    if (currentStep < steps) {
                        currentStep++
                    } else {
                        viewModel.addAccountToList(
                            onSuccess = onBackToAccounts,
                            onFailure = { /* Handle error */ }
                        )
                    }
                }
            )
        }
    }
}









