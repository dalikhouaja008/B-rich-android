package com.example.b_rich.ui.AddAccount

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccountScreen(viewModel: AddAccountViewModel) {
    val currentStep = viewModel.currentStep.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Bank Account") },
                actions = {
                    TextButton(onClick = { /* Handle Cancel Action */ }) {
                        Text("Cancel", color = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Progress Bar
            StepProgressBar(currentStep = currentStep, totalSteps = viewModel.totalSteps)

            Spacer(modifier = Modifier.height(32.dp))

            // Current Step Content
            when (currentStep) {
                1 -> StepOneContent(
                    accountName = viewModel.accountName.value,
                    onAccountNameChange = { viewModel.accountName.value = it }
                )
                2 -> StepTwoContent(
                    accountNumber = viewModel.accountNumber.value,
                    onAccountNumberChange = { viewModel.accountNumber.value = it }
                )
                3 -> StepThreeContent(
                    accountType = viewModel.accountType.value,
                    onAccountTypeChange = { viewModel.accountType.value = it },
                    accountTypes = viewModel.getAccountTypes()
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Navigation Buttons
            StepNavigationButtons(
                currentStep = currentStep,
                totalSteps = viewModel.totalSteps,
                onBack = { viewModel.currentStep.value -= 1 },
                onNext = {
                    if (currentStep < viewModel.totalSteps) {
                        viewModel.currentStep.value += 1
                    } else {
                        viewModel.saveAccount()
                    }
                }
            )
        }
    }
}

@Composable
fun StepOneContent(
    accountName: String,
    onAccountNameChange: (String) -> Unit
) {
    Column(horizontalAlignment = Alignment.Start) {
        Text("Step 1", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(130.dp))
        OutlinedTextField(
            value = accountName,
            onValueChange = { onAccountNameChange(it) },
            label = { Text("Account Name") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
fun StepTwoContent(
    accountNumber: String,
    onAccountNumberChange: (String) -> Unit
) {
    Column(horizontalAlignment = Alignment.Start) {
        Text("Step 2", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = accountNumber,
            onValueChange = { onAccountNumberChange(it) },
            label = { Text("Account Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
fun StepThreeContent(
    accountType: String,
    onAccountTypeChange: (String) -> Unit,
    accountTypes: List<String>
) {
    Column(horizontalAlignment = Alignment.Start) {
        Text("Step 3", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Select Account Type", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))

        accountTypes.forEach { type ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = accountType == type,
                    onClick = { onAccountTypeChange(type) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(type)
            }
        }
    }
}


@Composable
fun StepNavigationButtons(
    currentStep: Int,
    totalSteps: Int,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (currentStep > 1) {
            Button(onClick = onBack, modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                Text("Back")
            }
        }
        Button(onClick = onNext, modifier = Modifier.weight(1f).padding(start = 8.dp)) {
            Text(if (currentStep == totalSteps) "Finish" else "Next")
        }
    }
}

@Composable
fun StepProgressBar(currentStep: Int, totalSteps: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (step in 1..totalSteps) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .padding(horizontal = 4.dp)
                    .background(
                        color = if (step <= currentStep) MaterialTheme.colorScheme.primary else Color.Gray.copy(
                            alpha = 0.3f
                        ),
                        shape = MaterialTheme.shapes.small
                    )
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun AddAccountScreenPreview() {
    AddAccountScreen(viewModel = AddAccountViewModel())
}
