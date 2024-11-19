package com.example.b_rich.ui.signup


import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState

import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.b_rich.R
import com.example.b_rich.data.entities.user
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    viewModel: SignupViewModel = viewModel(),
    navHostController: NavHostController
) {
    val coroutineScope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var num by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var numError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }
    val context = LocalContext.current
    val signUpUiState by viewModel.signUpUiState.observeAsState(SignUpUiState())
    val scrollState = rememberScrollState()
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

    // Naviguer vers la page principale après l'inscription
    LaunchedEffect(key1 = signUpUiState.isSignedUp) {
        if (signUpUiState.isSignedUp) {
            navHostController.navigate("loginPage") {
                popUpTo("signUpPage") { inclusive = true }
            }
        }
    }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF3D5AFE), Color(0xFFB39DDB))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .verticalScroll(scrollState) // Make the Column scrollable
                    .background(Color.White, shape = MaterialTheme.shapes.medium)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3D5AFE),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Hello there, sign up to get started",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Logo Image (replace with your logo resource)
                val logo: Painter = painterResource(id = R.drawable.logo)
                Image(painter = logo, contentDescription = "Logo", modifier = Modifier.size(200.dp))
                Spacer(modifier = Modifier.height(8.dp))

                // Name TextField

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    isError = nameError.isNotEmpty(),
                    label = { Text("Name") },
                    leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = "Name Icon") },
                    placeholder = { Text("Name") },
                    modifier = Modifier.fillMaxWidth().padding(2.dp),
                    shape = MaterialTheme.shapes.medium
                )
                if (nameError.isNotEmpty()) {
                    Text(nameError, color = Color.Red)
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Email TextField
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    isError = emailError.isNotEmpty(),
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = "Email Icon") },
                    placeholder = { Text("Email") },
                    modifier = Modifier.fillMaxWidth().padding(2.dp),
                    shape = MaterialTheme.shapes.medium,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                    ),
                )
                if (emailError.isNotEmpty()) {
                    Text(emailError, color = Color.Red)
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Phone Number TextField
                OutlinedTextField(
                    value = num,
                    onValueChange = { num = it },
                    isError = numError.isNotEmpty(),
                    label = { Text("Phone Number") },
                    leadingIcon = { Icon(Icons.Outlined.Phone, contentDescription = "Phone Icon") },
                    placeholder = { Text("Phone number") },
                    modifier = Modifier.fillMaxWidth().padding(2.dp),
                    shape = MaterialTheme.shapes.medium,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                    ),
                )
                if (numError.isNotEmpty()) {
                    Text(numError, color = Color.Red)
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Password TextField
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    isError = passwordError.isNotEmpty(),
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = "Password Icon") },
                    placeholder = { Text("Password") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                    ),
                    visualTransformation =
                    if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon =
                    {
                        val visibilityIcon =
                            if (passwordVisible) Icons.Outlined.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = visibilityIcon, contentDescription = "Toggle Password Visibility")
                        }
                    },
                    modifier= Modifier.fillMaxWidth().padding(2.dp),
                )
                if (passwordError.isNotEmpty()) {
                    Text(passwordError, color=Color.Red)
                }
                Spacer(modifier=Modifier.height(8.dp))

                // Confirm Password TextField
                OutlinedTextField(
                    value=confirmPassword,
                    onValueChange={confirmPassword=it},
                    isError=confirmPasswordError.isNotEmpty(),
                    label={Text("Confirm Password")},
                    leadingIcon={Icon(Icons.Outlined.Lock, contentDescription="Confirm Password Icon")},
                    placeholder={Text("Confirm Password")},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                    ),
                    visualTransformation=
                    if(confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon=
                    {
                        val visibilityIcon=
                            if(confirmPasswordVisible) Icons.Outlined.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick={confirmPasswordVisible=!confirmPasswordVisible}) {
                            Icon(imageVector=visibilityIcon, contentDescription="Toggle Confirm Password Visibility")
                        }
                    },
                    modifier=Modifier.fillMaxWidth().padding(2.dp),
                )
                if(confirmPasswordError.isNotEmpty()) {
                    Text(confirmPasswordError, color=Color.Red)
                }
                Spacer(modifier=Modifier.height(16.dp))

                // Sign Up Button
                Button(
                    onClick={
                        val isNameValid=viewModel.validateName(name){nameError=it}
                        val isEmailValid=viewModel.validateEmail(email){emailError=it}
                        val isPasswordValid=viewModel.validatePassword(password){passwordError=it}
                        val isConfirmPasswordValid=viewModel.validateConfirmPassword(password, confirmPassword){confirmPasswordError=it}
                        if(isNameValid && isEmailValid && isPasswordValid && isConfirmPasswordValid){
                            val user= user(name=name,email=email,numTel=num,password=password)
                            viewModel.createUser(user)
                        }
                    },
                    modifier=Modifier.fillMaxWidth().height(48.dp),
                    colors=ButtonDefaults.buttonColors(containerColor=Color(0xFFF44336))
                ) {
                    if(viewModel.signUpUiState.value?.isLoading == true){
                        CircularProgressIndicator(color=MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text("Sign Up")
                    }
                }

                Spacer(modifier=Modifier.height(8.dp))

                // Error Message Display
                viewModel.signUpUiState.value?.errorMessage?.let{ errorMessage ->
                    Text(text=errorMessage,color=MaterialTheme.colorScheme.error)
                }

                Spacer(modifier=Modifier.height(16.dp))

                // Sign In Link
                Row(
                    modifier=Modifier.fillMaxWidth(),
                    horizontalArrangement=Arrangement.Center,
                    verticalAlignment=Alignment.CenterVertically
                ) {
                    Text(text="Already have an account?", color=Color.Gray)
                    Spacer(modifier=Modifier.width(4.dp))
                    ClickableText(
                        text=AnnotatedString("Sign In"),
                        onClick={
                            navHostController.navigate("loginPage")
                        },
                        style=MaterialTheme.typography.bodySmall.copy(color=Color(0xFF3D5AFE), fontWeight=FontWeight.Bold)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Or sign up with", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))
                // Google Sign-In Button (optional)
                // Social Media Icons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                    modifier = Modifier.fillMaxWidth()
                ) {
                   /* GoogleLoginButton(
                        onClick = {
                            val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                                .setFilterByAuthorizedAccounts(false) // Query all google accounts on the device
                                .setServerClientId(SERVER_CLIENT_ID)
                                .build()

                            val request = GetCredentialRequest.Builder()
                                .addCredentialOption(googleIdOption)
                                .build()

                            val credentialManager = CredentialManager.create(context)

                            coroutineScope.launch {
                                try {
                                    val result = credentialManager.getCredential(context, request)
                                    viewModel.handleSignIn(result)
                                } catch (e: GetCredentialException) {
                                    Log.e("MainActivity", "GetCredentialException", e)
                                }
                            }
                        }
                    )*/
                }
            }
        }
    }

@Composable
fun GoogleLoginButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text("Sign in with Google")
        // Optionally, add an image/icon for visual representation
        Image(
            painter = painterResource(id = R.drawable.ic_google), // Replace with your drawable resource for the Google icon.
            contentDescription = "Google Sign-In",
            modifier = Modifier.size(24.dp)
        )
    }
}