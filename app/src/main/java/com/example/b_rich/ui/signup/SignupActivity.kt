package com.example.b_rich.ui.signup

import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.b_rich.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException


@Composable
fun SignUpScreen(navHostController: NavHostController) {
    val context = LocalContext.current
    val activity = context as ComponentActivity

    // Configure Google Sign-In
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()
    val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(context, gso)

    // Launcher for Google Sign-In
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            // Handle successful sign-in (e.g., access account.email or account.displayName)
        } catch (e: ApiException) {
            // Handle sign-in failure
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Welcome to us",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 28.sp, color = Color(0xFF333333))
        )
        Text(
            text = "Hello there, create New account",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(2.dp))

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .size(250.dp)
                .padding(10.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        StyledTextField(value = "", onValueChange = {}, placeholder = "Name")
        Spacer(modifier = Modifier.height(12.dp))

        StyledTextField(value = "", onValueChange = {}, placeholder = "Email")
        Spacer(modifier = Modifier.height(12.dp))

        StyledTextField(value = "", onValueChange = {}, placeholder = "Password", isPassword = true)
        Spacer(modifier = Modifier.height(12.dp))

        StyledTextField(value = "", onValueChange = {}, placeholder = "Rewrite Password", isPassword = true)

        Spacer(modifier = Modifier.height(45.dp))

        Button(
            onClick = { /* Handle Sign Up Action */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(text = "Sign up", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Or sign up with", fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "You already have an account!", color = Color.Gray)
            Spacer(modifier = Modifier.width(4.dp))
            ClickableText(
                text = AnnotatedString("Sign in"),
                onClick = {navHostController.navigate("loginPage") },
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF3D5AFE),
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Social Media Icons
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "Sign up with Google",
                modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        // Trigger Google Sign-In Intent
                        launcher.launch(googleSignInClient.signInIntent)
                    }
            )
        }
    }
}

@Composable
fun StyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false
) {
    val visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFEDEDED), shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        textStyle = TextStyle(fontSize = 16.sp),
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (value.isEmpty()) {
                    Text(text = placeholder, color = Color.Gray)
                }
                innerTextField()
            }
        },
        visualTransformation = visualTransformation
    )
}
/*@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen()
}*/
