package com.example.b_rich.ui.AddAccount

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import com.example.b_rich.ui.AddAccount.componenets.AccountCustomizationStep
import com.example.b_rich.ui.AddAccount.componenets.AccountVerificationStep
import com.example.b_rich.ui.AddAccount.componenets.AddAccountTopBar
import com.example.b_rich.ui.AddAccount.componenets.LinkAccountButton
import com.example.b_rich.ui.AddAccount.componenets.RibEntryStep
import com.example.b_rich.ui.AddAccount.componenets.VerticalStepIndicator
import com.example.b_rich.ui.AddAccount.componenets.WelcomeStep

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccountScreen(
    viewModel: AddAccountViewModel,
    onBackToAccounts: () -> Unit
) {
    val steps = remember { listOf("Welcome", "Enter RIB", "Verification", "Customize") }
    var currentStep by remember { mutableStateOf(0) }
    val scrollState = rememberScrollState()
    val isRibValid by viewModel.isRibValid.collectAsState()

    val gradientColors = listOf(
        Color(0xFF6200EE),
        Color(0xFF9C27B0),
        Color(0xFF3700B3)
    )

    Scaffold(
        topBar = {
            AddAccountTopBar(
                onBackToAccounts = onBackToAccounts,
                currentStep = steps[currentStep]
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Start
            ) {
                // Left side - Progress panel with increased width
                Card(
                    modifier = Modifier
                        .width(240.dp) // Augmenté à 240dp pour plus d'espace
                        .fillMaxHeight()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 16.dp, horizontal = 16.dp) // Padding horizontal augmenté
                    ) {
                        Text(
                            text = "Progress",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(start = 8.dp, bottom = 16.dp)
                        )

                        VerticalStepIndicator(
                            steps = steps,
                            currentStep = currentStep,
                            gradientColors = gradientColors,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Right side content
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .verticalScroll(scrollState),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        AnimatedContent(
                            targetState = currentStep,
                            label = "step_transition"
                        ) { step ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                when (step) {
                                    0 -> WelcomeStep()
                                    1 -> RibEntryStep(viewModel)
                                    2 -> AccountVerificationStep(viewModel)
                                    3 -> AccountCustomizationStep(viewModel)
                                }
                            }
                        }
                    }

                    // Navigation buttons with gradient overlay
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                                        MaterialTheme.colorScheme.surface
                                    ),
                                    startY = 0f,
                                    endY = 100f
                                )
                            )
                            .padding(16.dp)
                    ) {
                        LinkAccountButton(
                            currentStep = currentStep,
                            totalSteps = steps.size,
                            isEnabled = when (currentStep) {
                                0 -> true
                                1 -> isRibValid
                                2 -> viewModel.accountDetails.value != null
                                3 -> viewModel.nickname.value.isNotBlank()
                                else -> false
                            },
                            onNext = {
                                if (currentStep < steps.size - 1) {
                                    currentStep++
                                } else {
                                    viewModel.linkAccount(onSuccess = onBackToAccounts)
                                }
                            },
                            onBack = {
                                if (currentStep > 0) {
                                    currentStep--
                                }
                            },
                            gradientColors = gradientColors
                        )
                    }
                }
            }
        }
    }
}














