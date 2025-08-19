package com.memeapp.memeflex.viewmodel

// viewmodel/SplashViewModel.kt (Optional - for more complex splash logic)
package com.memeapp.memeflex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memeapp.memeflex.data.repository.AuthRepository
import com.memeapp.memeflex.data.local.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import javax.inject.Inject

data class SplashUiState(
    val isLoading: Boolean = true,
    val isLoggedIn: Boolean = false,
    val hasCompletedOnboarding: Boolean = true, // For future onboarding flow
    val appVersion: String = "",
    val shouldNavigate: Boolean = false,
    val navigationDestination: SplashDestination = SplashDestination.LOGIN
)

enum class SplashDestination {
    LOGIN,
    ONBOARDING, // For future use
    FEED
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    init {
        initializeApp()
    }

    private fun initializeApp() {
        viewModelScope.launch {
            // Combine multiple checks
            combine(
                authRepository.isLoggedIn(),
                authRepository.getCurrentUser(),
                // You can add more flows here like:
                // userPreferences.hasCompletedOnboarding(),
                // checkForUpdates(),
                // preloadData()
            ) { isLoggedIn, currentUser ->
                SplashUiState(
                    isLoading = false,
                    isLoggedIn = isLoggedIn && currentUser != null,
                    shouldNavigate = true,
                    navigationDestination = if (isLoggedIn && currentUser != null) {
                        SplashDestination.FEED
                    } else {
                        SplashDestination.LOGIN
                    }
                )
            }
                .onStart {
                    // Ensure minimum splash duration
                    delay(2000)
                }
                .collect { newState ->
                    _uiState.value = newState
                }
        }
    }

    // Optional: Add methods for additional splash logic
    fun checkAppUpdates() {
        // Check for app updates
    }

    fun preloadCriticalData() {
        // Preload any critical app data
    }

    fun validateSession() {
        viewModelScope.launch {
            // Validate if stored session is still valid
            // Make a quick API call to verify token
        }
    }
}

// Alternative SplashScreen using dedicated ViewModel
@Composable
fun SplashScreenWithViewModel(
    modifier: Modifier = Modifier,
    onNavigateToLogin: () -> Unit = {},
    onNavigateToFeed: () -> Unit = {},
    onNavigateToOnboarding: () -> Unit = {},
    splashViewModel: SplashViewModel = hiltViewModel()
) {
    val uiState by splashViewModel.uiState.collectAsState()

    // Handle navigation
    LaunchedEffect(uiState.shouldNavigate) {
        if (uiState.shouldNavigate) {
            delay(300) // Small delay for smooth transition

            when (uiState.navigationDestination) {
                SplashDestination.LOGIN -> onNavigateToLogin()
                SplashDestination.FEED -> onNavigateToFeed()
                SplashDestination.ONBOARDING -> onNavigateToOnboarding()
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            AppIcon(modifier = Modifier.size(200.dp))

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "MemeFlex",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Share the laughs",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dynamic status text based on state
            Text(
                text = when {
                    uiState.isLoading -> "Initializing..."
                    uiState.isLoggedIn -> "Welcome back!"
                    else -> "Getting ready..."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        }

        LoadingIndicator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(100.dp)
        )
    }
}