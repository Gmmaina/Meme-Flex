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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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

data class RegisterState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisible: Boolean = false
)

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onRegister: (String, String) -> Unit = { _, _ -> },
    onLogin: () -> Unit = {}
) {
    var registerState by remember { mutableStateOf(RegisterState()) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
//            Spacer(modifier = Modifier.height(100.dp))
            Text(
                "JOIN MEME FLEX.",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            Text(
                "Sign up to continue sharing memes.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            InputLayout(
                registerState = registerState,
                onRegisterStateChange = { registerState = it },
                onRegister = { onRegister(registerState.username, registerState.password) },
                onLogin = onLogin
            )
        }
    }


}

@Composable
fun InputLayout(
    modifier: Modifier = Modifier,
    registerState: RegisterState,
    onRegisterStateChange: (RegisterState) -> Unit,
    onRegister: () -> Unit,
    onLogin: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            OutlinedTextField(
                value = registerState.username,
                onValueChange = {
                    onRegisterStateChange(registerState.copy(username = it))
                },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = registerState.email,
                onValueChange = {
                    onRegisterStateChange(registerState.copy(email = it))
                },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null)
                },
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = registerState.password,
                onValueChange = {
                    onRegisterStateChange(registerState.copy(password = it))
                },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = {
                    Icon(Icons.Default.Password, contentDescription = null)
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            onRegisterStateChange(
                                registerState.copy(passwordVisible = !registerState.passwordVisible)
                            )
                        }
                    ) {
                        Icon(
                            imageVector = if (registerState.passwordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = if (registerState.passwordVisible)
                                "Hide password"
                            else
                                "Show password"
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = registerState.confirmPassword,
                onValueChange = {
                    onRegisterStateChange(registerState.copy(confirmPassword = it))
                },
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = {
                    Icon(Icons.Default.Password, contentDescription = null)
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            onRegisterStateChange(
                                registerState.copy(passwordVisible = !registerState.passwordVisible)
                            )
                        }
                    ) {
                        Icon(
                            imageVector = if (registerState.passwordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = if (registerState.passwordVisible)
                                "Hide password"
                            else
                                "Show password"
                        )
                    }
                },
                visualTransformation = if (registerState.passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                enabled = registerState.username.isNotBlank() &&
                        registerState.email.isNotBlank() &&
                        registerState.password.isNotBlank() &&
                        registerState.confirmPassword.isNotBlank(),
                onClick = onRegister,
            ) {
                Text(
                    "Register",
                    style = MaterialTheme.typography.displayMedium
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    "Already have an account?"
                )
                Text(
                    "Sign In",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable { onLogin() }
                )
            }
        }
    }
}

@Preview
@Composable
fun InputLayoutPreview1() {
    MemeFlexTheme(false) {
        RegisterScreen(
            onRegister = { username, password ->
                println("Register")
            },
            onLogin = {
                println("Sign in")
            }
        )
    }
}