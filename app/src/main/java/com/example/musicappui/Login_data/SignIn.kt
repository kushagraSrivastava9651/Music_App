package com.example.musicappui.Login_data

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

/*
@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onNavigateToSignUp: () -> Unit,
    onSignInSuccess: () -> Unit
) {
    val result by authViewModel.authResult.observeAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sign In",
            style = MaterialTheme.typography.h4.copy(
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = "Toggle password visibility")
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )
        Button(
            onClick = {
                authViewModel.login(email, password)
                when (result) {
                    is Result.Success -> {
                        onSignInSuccess()
                    }
                    is Result.Error -> {
                        // Handle error case
                    }
                    else -> {
                        // Handle other cases
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)

        ) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Don't have an account? Sign up.",
            modifier = Modifier.clickable {
                onNavigateToSignUp()
            },
            style = MaterialTheme.typography.body2.copy(
                color = MaterialTheme.colors.primary
            )
        )
    }
}

 */
@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onNavigateToSignUp: () -> Unit,
    onSignInSuccess: () -> Unit
) {
    val result by authViewModel.authResult.observeAsState()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header text
        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.h5.copy(
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Enter your credential to login",
            style = MaterialTheme.typography.body2.copy(
                color = Color.Gray
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
        )

        // Username field
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Username"
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )

        // Password field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Password"
                )
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = "Toggle password visibility")
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )

        // Login Button
        Button(
            onClick = {
                authViewModel.login(username, password)
                when (result) {
                    is Result.Success -> {
                        onSignInSuccess()
                    }
                    is Result.Error -> {
                        // Handle error case
                    }
                    else -> {
                        // Handle other cases
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF6A1B9A), // Purple shade
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(50) // Rounded button
        ) {
            Text("Login")
        }

        // Forgot Password
        Text(
            text = "Forgot password?",
            style = MaterialTheme.typography.body2.copy(
                color = Color(0xFF6A1B9A)
            ),
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable {
                    // Handle forgot password action
                }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sign Up Link
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Don't have an account? ")
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.body2.copy(
                    color = Color(0xFF6A1B9A),
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.clickable {
                    onNavigateToSignUp()
                }
            )
        }
    }
}

