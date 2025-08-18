package com.memeapp.memeflex.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.memeapp.memeflex.ui.theme.MemeFlexTheme

// Data class to hold login state
data class LoginState(
    val username: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false
)

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLogin: (String, String) -> Unit = { _, _ -> },
    onSignUp: () -> Unit = {},
    onForgotPassword: () -> Unit = {}
) {
    // State is now hoisted to the Login composable
    var loginState by remember { mutableStateOf(LoginState()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(150.dp))
            Text(
                "WELCOME BACK.",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Sign in to continue sharing memes.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            InputLayout(
                loginState = loginState,
                onLoginStateChange = { loginState = it },
                onLogin = { onLogin(loginState.username, loginState.password) },
                onSignUp = onSignUp,
                onForgotPassword = onForgotPassword
            )
        }
    }
}

@Composable
fun InputLayout(
    loginState: LoginState,
    onLoginStateChange: (LoginState) -> Unit,
    onLogin: () -> Unit,
    onSignUp: () -> Unit,
    onForgotPassword: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp),
    ) {
        Column(
            Modifier.padding(24.dp),
        ) {
            OutlinedTextField(
                value = loginState.username,
                onValueChange = {
                    onLoginStateChange(loginState.copy(username = it))
                },
                label = { Text("Username or Email") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = loginState.password,
                onValueChange = {
                    onLoginStateChange(loginState.copy(password = it))
                },
                label = { Text("Password") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null)
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            onLoginStateChange(
                                loginState.copy(passwordVisible = !loginState.passwordVisible)
                            )
                        }
                    ) {
                        Icon(
                            imageVector = if (loginState.passwordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = if (loginState.passwordVisible)
                                "Hide password"
                            else
                                "Show password"
                        )
                    }
                },
                visualTransformation = if (loginState.passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                modifier = Modifier.align(Alignment.End),
                onClick = onForgotPassword
            ) {
                Text("Forgot Password?")
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                enabled = loginState.username.isNotBlank() && loginState.password.isNotBlank(),
                onClick = onLogin,
            ) {
                Text(
                    "Login",
                    style = MaterialTheme.typography.displayMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Don't have an account?")
                Text(
                    "Sign Up",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable { onSignUp() }
                )
            }
        }
    }
}

@Preview
@Composable
fun InputLayoutPreview() {
    MemeFlexTheme(false) {
        LoginScreen(
            onLogin = { username, password ->
                println("Login attempt: $username")
            },
            onSignUp = {
                println("Navigate to Sign Up")
            },
            onForgotPassword = {
                println("Navigate to Forgot Password")
            }
        )
    }
}