package com.example.b_rich.ui.listAccounts.componenets

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.example.b_rich.ui.biometricDialog.BiometricAuthenticator
import com.example.b_rich.ui.components.SectionTitle

@Composable
public fun AnimatedHeader(
    onAddAccountClick: () -> Unit,
    gradientColors: List<Color>
) {
    // Configuration de l'authentification biométrique
    val context = LocalContext.current
    val activity = LocalContext.current as FragmentActivity
    val biometricAuthenticator = remember { BiometricAuthenticator(context) }

    fun handleBiometricAuth() {
        biometricAuthenticator.promptBiometricAuth(
            title = "Add Account Authentication",
            subTitle = "Please authenticate to add a new account",
            negativeButtonText = "Cancel",
            fragmentActivity = activity,
            onSuccess = {
                // Appeler la fonction onAddAccountClick après authentification réussie
                onAddAccountClick()
            },
            onFailed = {
                Toast.makeText(
                    context,
                    "Authentication failed",
                    Toast.LENGTH_SHORT
                ).show()
            },
            onError = { _, error ->
                Toast.makeText(
                    context,
                    "Error: $error",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .animateContentSize(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                SectionTitle(
                    title = "My accounts",
                    description = "Here you can manage your finances."
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Bouton Add Account avec authentification
            Button(
                onClick = { handleBiometricAuth() }, // Appel direct de handleBiometricAuth
                modifier = Modifier.wrapContentWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3D5AFE),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Add Account",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}