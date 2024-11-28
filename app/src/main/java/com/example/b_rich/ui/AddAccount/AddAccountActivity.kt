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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccountScreen(viewModel: AddAccountViewModel = remember { AddAccountViewModel() }) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Account") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFFEFEFEF))
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.White, Color(0xFFEEEEEE))
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Step Progress Bar
                    StepProgressBar(
                        currentStep = viewModel.currentStep.value,
                        totalSteps = viewModel.totalSteps
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Step Content
                    Column(modifier = Modifier.weight(1f)) {
                        for (step in 1..viewModel.totalSteps) {
                            StepView(
                                isCurrentStep = viewModel.currentStep.value == step,
                                stepNumber = step,
                                title = viewModel.getStepTitle(step),
                                content = {
                                    StepContent(step, viewModel)
                                },
                                onStepTapped = { viewModel.currentStep.value = step }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Navigation Buttons
                    Button(
                        onClick = {
                            if (viewModel.currentStep.value < viewModel.totalSteps) {
                                viewModel.currentStep.value += 1
                            } else {
                                viewModel.saveAccount()
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
                            fontSize = 16.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    )
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
            .padding(vertical = 8.dp)
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

@Composable
fun StepContent(step: Int, viewModel: AddAccountViewModel) {
    when (step) {
        1 -> {
            OutlinedTextField(
                value = viewModel.accountName.value,
                onValueChange = { viewModel.accountName.value = it },
                label = { Text("Enter Account Name") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        2 -> {
            OutlinedTextField(
                value = viewModel.accountNumber.value,
                onValueChange = { viewModel.accountNumber.value = it },
                label = { Text("Enter Account Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }
        3 -> {
            Column {
                Text(
                    text = "Enter the 6-digit OTP sent to your phone",
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
                            modifier = Modifier.width(48.dp),
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
@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewAddAccountScreenStep1() {
    val mockViewModel = AddAccountViewModel().apply {
        accountName.value = "John Doe"
        accountNumber.value = ""
        currentStep.value = 1
    }

    AddAccountScreen(viewModel = mockViewModel)
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewAddAccountScreenStep2() {
    val mockViewModel = AddAccountViewModel().apply {
        accountName.value = "John Doe"
        accountNumber.value = "12345678"
        currentStep.value = 2
    }

    AddAccountScreen(viewModel = mockViewModel)
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewAddAccountScreenStep3() {
    val mockViewModel = AddAccountViewModel().apply {
        accountName.value = "John Doe"
        accountNumber.value = "12345678"
        otp.value = MutableList(6) { if (it < 4) "1" else "" }
        currentStep.value = 3
    }

    AddAccountScreen(viewModel = mockViewModel)
}