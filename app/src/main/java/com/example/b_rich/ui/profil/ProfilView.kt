package com.example.b_rich.ui.profil

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.b_rich.data.entities.user
import com.example.b_rich.navigateToCodeVerification
import com.example.b_rich.ui.resetPassword.ResetPasswordViewModel
import com.example.b_rich.ui.theme.PREF_FILE

@Composable
fun UserProfileScreen(
    user: user,
    viewModel: ResetPasswordViewModel = viewModel(),
    navHostController: NavHostController,
    context: Context
) {
    val mSharedPreferences = remember { context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Titre de la page
        Text(
            text = "Profil de ${user.name}",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3D5AFE),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Affichage des informations de l'utilisateur
        Text(
            text = "Email : ${user.email}",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Téléphone : ${user.numTel}",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Bouton pour réinitialiser le mot de passe
        ClickableText(
            text = AnnotatedString("Réinitialiser le mot de passe"),
            onClick = {
                viewModel.requestReset(user.email)
                navigateToCodeVerification(user, navHostController)
            },
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xFF3D5AFE),
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Bouton pour se déconnecter
        ClickableText(
            text = AnnotatedString("Se déconnecter"),
            onClick = {
                val editor = mSharedPreferences.edit()
                editor.clear()
                editor.apply()
                (context as? Activity)?.finishAffinity()
            },
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xFF3D5AFE),
                fontWeight = FontWeight.Bold
            )
        )
    }
}