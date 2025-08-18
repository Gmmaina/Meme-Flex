package com.memeapp.memeflex.ui.auth

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Data classes for state management
data class ForgotPasswordState(
    val email: String = "",
    val otp: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val newPasswordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val step: ForgotPasswordStep = ForgotPasswordStep.EMAIL_INPUT,
    val otpTimeLeft: Int = 0
)

enum class ForgotPasswordStep {
    EMAIL_INPUT,
    OTP_VERIFICATION,
    PASSWORD_RESET,
    SUCCESS
}

@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onPasswordResetSuccess: () -> Unit = {}
) {
    var state by remember { mutableStateOf(ForgotPasswordState()) }

    // OTP countdown timer
    LaunchedEffect(state.otpTimeLeft) {
        if (state.otpTimeLeft > 0) {
            delay(1000)
            state = state.copy(otpTimeLeft = state.otpTimeLeft - 1)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Back button
        IconButton(
            onClick = onNavigateBack,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (state.step) {
                ForgotPasswordStep.EMAIL_INPUT -> {
                    EmailInputStep(
                        state = state,
                        onStateChange = { state = it }
                    )
                }
                ForgotPasswordStep.OTP_VERIFICATION -> {
                    OtpVerificationStep(
                        state = state,
                        onStateChange = { state = it }
                    )
                }
                ForgotPasswordStep.PASSWORD_RESET -> {
                    PasswordResetStep(
                        state = state,
                        onStateChange = { state = it }
                    )
                }
                ForgotPasswordStep.SUCCESS -> {
                    SuccessStep(
                        onContinue = onPasswordResetSuccess
                    )
                }
            }
        }
    }
}

@Composable
private fun EmailInputStep(
    state: ForgotPasswordState,
    onStateChange: (ForgotPasswordState) -> Unit
) {
    Text(
        "Forgot Password?",
        style = MaterialTheme.typography.displaySmall,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onBackground
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        "Enter your email address and we'll send you an OTP to reset your password.",
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
    )

    Spacer(modifier = Modifier.height(32.dp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            // Error message
            state.errorMessage?.let { error ->
                ErrorCard(
                    message = error,
                    onDismiss = { onStateChange(state.copy(errorMessage = null)) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            OutlinedTextField(
                value = state.email,
                onValueChange = {
                    onStateChange(state.copy(email = it, errorMessage = null))
                },
                label = { Text("Email Address") },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                enabled = !state.isLoading,
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (isValidEmail(state.email)) {
                        onStateChange(
                            state.copy(
                                isLoading = true,
                                errorMessage = null
                            )
                        )
                        // Simulate API call
                        simulateEmailVerification(
                            onSuccess = {
                                onStateChange(
                                    state.copy(
                                        isLoading = false,
                                        step = ForgotPasswordStep.OTP_VERIFICATION,
                                        otpTimeLeft = 300 // 5 minutes
                                    )
                                )
                            },
                            onError = { error ->
                                onStateChange(
                                    state.copy(
                                        isLoading = false,
                                        errorMessage = error
                                    )
                                )
                            }
                        )
                    } else {
                        onStateChange(state.copy(errorMessage = "Please enter a valid email address"))
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = state.email.isNotBlank() && !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        "Send OTP",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun OtpVerificationStep(
    state: ForgotPasswordState,
    onStateChange: (ForgotPasswordState) -> Unit
) {
    Text(
        "Verify OTP",
        style = MaterialTheme.typography.displaySmall,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onBackground
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        "Enter the 6-digit code sent to ${state.email}",
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
    )

    Spacer(modifier = Modifier.height(32.dp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            // Error message
            state.errorMessage?.let { error ->
                ErrorCard(
                    message = error,
                    onDismiss = { onStateChange(state.copy(errorMessage = null)) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            OutlinedTextField(
                value = state.otp,
                onValueChange = {
                    if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                        onStateChange(state.copy(otp = it, errorMessage = null))
                    }
                },
                label = { Text("Enter OTP") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                enabled = !state.isLoading,
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Timer and resend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (state.otpTimeLeft > 0) {
                    Text(
                        "Resend in ${formatTime(state.otpTimeLeft)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                } else {
                    TextButton(
                        onClick = {
                            onStateChange(state.copy(otpTimeLeft = 300))
                            // Simulate resend OTP
                        }
                    ) {
                        Text("Resend OTP")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (state.otp.length == 6) {
                        onStateChange(state.copy(isLoading = true, errorMessage = null))
                        // Simulate OTP verification
                        simulateOtpVerification(
                            otp = state.otp,
                            onSuccess = {
                                onStateChange(
                                    state.copy(
                                        isLoading = false,
                                        step = ForgotPasswordStep.PASSWORD_RESET
                                    )
                                )
                            },
                            onError = { error ->
                                onStateChange(
                                    state.copy(
                                        isLoading = false,
                                        errorMessage = error
                                    )
                                )
                            }
                        )
                    } else {
                        onStateChange(state.copy(errorMessage = "Please enter a valid 6-digit OTP"))
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = state.otp.length == 6 && !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        "Verify OTP",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun PasswordResetStep(
    state: ForgotPasswordState,
    onStateChange: (ForgotPasswordState) -> Unit
) {
    Text(
        "Reset Password",
        style = MaterialTheme.typography.displaySmall,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onBackground
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        "Enter your new password below.",
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
    )

    Spacer(modifier = Modifier.height(32.dp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            // Error message
            state.errorMessage?.let { error ->
                ErrorCard(
                    message = error,
                    onDismiss = { onStateChange(state.copy(errorMessage = null)) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            OutlinedTextField(
                value = state.newPassword,
                onValueChange = {
                    onStateChange(state.copy(newPassword = it, errorMessage = null))
                },
                label = { Text("New Password") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null)
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            onStateChange(state.copy(newPasswordVisible = !state.newPasswordVisible))
                        }
                    ) {
                        Icon(
                            imageVector = if (state.newPasswordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = if (state.newPasswordVisible)
                                "Hide password"
                            else
                                "Show password"
                        )
                    }
                },
                visualTransformation = if (state.newPasswordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                enabled = !state.isLoading,
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.confirmPassword,
                onValueChange = {
                    onStateChange(state.copy(confirmPassword = it, errorMessage = null))
                },
                label = { Text("Confirm Password") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null)
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            onStateChange(state.copy(confirmPasswordVisible = !state.confirmPasswordVisible))
                        }
                    ) {
                        Icon(
                            imageVector = if (state.confirmPasswordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = if (state.confirmPasswordVisible)
                                "Hide password"
                            else
                                "Show password"
                        )
                    }
                },
                visualTransformation = if (state.confirmPasswordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                enabled = !state.isLoading,
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    when {
                        state.newPassword.length < 8 -> {
                            onStateChange(state.copy(errorMessage = "Password must be at least 8 characters"))
                        }
                        state.newPassword != state.confirmPassword -> {
                            onStateChange(state.copy(errorMessage = "Passwords do not match"))
                        }
                        else -> {
                            onStateChange(state.copy(isLoading = true, errorMessage = null))
                            // Simulate password reset
                            simulatePasswordReset(
                                onSuccess = {
                                    onStateChange(
                                        state.copy(
                                            isLoading = false,
                                            step = ForgotPasswordStep.SUCCESS
                                        )
                                    )
                                },
                                onError = { error ->
                                    onStateChange(
                                        state.copy(
                                            isLoading = false,
                                            errorMessage = error
                                        )
                                    )
                                }
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = state.newPassword.isNotBlank() &&
                        state.confirmPassword.isNotBlank() &&
                        !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        "Reset Password",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun SuccessStep(
    onContinue: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "✅",
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Password Reset Successful!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Your password has been successfully reset. You can now login with your new password.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                "Continue to Login",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
private fun ErrorCard(
    message: String,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.weight(1f)
            )
            TextButton(onClick = onDismiss) {
                Text("Dismiss")
            }
        }
    }
}

// Helper functions
private fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

private fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%d:%02d", minutes, remainingSeconds)
}

// Simulation functions (replace with actual API calls)
private fun simulateEmailVerification(
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    // Simulate API delay
    CoroutineScope(Dispatchers.Main).launch {
        delay(2000)
        onSuccess()
    }
}

private fun simulateOtpVerification(
    otp: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    CoroutineScope(Dispatchers.Main).launch {
        delay(1500)
        if (otp == "123456") { // Demo OTP
            onSuccess()
        } else {
            onError("Invalid OTP. Please try again.")
        }
    }
}

private fun simulatePasswordReset(
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    CoroutineScope(Dispatchers.Main).launch {
        delay(2000)
        onSuccess()
    }
}

@Preview(showBackground = true)
@Composable
private fun ForgotPasswordScreenPreview() {
    MaterialTheme {
        ForgotPasswordScreen()
    }
}