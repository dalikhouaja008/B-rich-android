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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddAccountTopBar(onBackToAccounts: () -> Unit) {
    TopAppBar(
        title = { Text("Add Account") },
        navigationIcon = {
            IconButton(onClick = onBackToAccounts) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
private fun ProgressIndicator(
    currentStep: Int,
    totalSteps: Int,
    gradientColors: List<Color>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .height(6.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(3.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(currentStep.toFloat() / totalSteps)
                .height(6.dp)
                .background(
                    brush = Brush.horizontalGradient(gradientColors),
                    shape = RoundedCornerShape(3.dp)
                )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    gradientColor: Color
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        placeholder = { Text(placeholder) },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = gradientColor,
            focusedLabelColor = gradientColor
        )
    )
}

@Composable
private fun GradientButton(
    text: String,
    showArrow: Boolean,
    enabled: Boolean,
    gradientColors: List<Color>,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.padding(20.dp)
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .background(
                    brush = Brush.horizontalGradient(gradientColors),
                    shape = RoundedCornerShape(16.dp)
                ),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            enabled = enabled
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                if (showArrow) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = "Next",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun StepCard(
    stepNumber: Int,
    isCurrentStep: Boolean,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrentStep)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            if (isCurrentStep)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.surfaceVariant,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "$stepNumber",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isCurrentStep)
                            Color.White
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Column {
                    Text(
                        title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = if (isCurrentStep)
                            MaterialTheme.colorScheme.onSurface
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            content()
        }
    }
}