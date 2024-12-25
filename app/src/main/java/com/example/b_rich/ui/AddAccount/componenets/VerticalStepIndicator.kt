package com.example.b_rich.ui.AddAccount.componenets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun VerticalStepIndicator(
    steps: List<String>,
    currentStep: Int,
    gradientColors: List<Color>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        steps.forEachIndexed { index, stepName ->
            StepItem(
                stepName = stepName,
                stepNumber = index + 1,
                isCompleted = index < currentStep,
                isActive = index == currentStep,
                isLast = index == steps.size - 1,
                primaryColor = gradientColors[0]
            )
        }
    }
}

@Composable
private fun StepItem(
    stepName: String,
    stepNumber: Int,
    isCompleted: Boolean,
    isActive: Boolean,
    isLast: Boolean,
    primaryColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp) // Augmenté l'espacement
        ) {
            // Circle indicator
            Box(
                modifier = Modifier
                    .size(40.dp) // Légèrement plus grand
                    .background(
                        color = when {
                            isCompleted -> primaryColor
                            isActive -> primaryColor.copy(alpha = 0.2f)
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        },
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = stepNumber.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isActive) primaryColor
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Step text content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stepName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                    color = if (isActive) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                AnimatedVisibility(visible = isActive) {
                    Text(
                        text = when (stepName) {
                            "Welcome" -> "Get started"
                            "Enter RIB" -> "Bank details"
                            "Verification" -> "Verify account"
                            "Customize" -> "Almost done"
                            else -> ""
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // Connector line
        if (!isLast) {
            Box(
                modifier = Modifier
                    .padding(start = 28.dp) // Ajusté pour l'alignement
                    .width(2.dp)
                    .height(24.dp)
                    .background(
                        color = if (isCompleted) primaryColor
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
            )
        }
    }
}