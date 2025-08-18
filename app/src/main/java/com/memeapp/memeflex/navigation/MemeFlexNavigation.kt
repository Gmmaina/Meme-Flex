package com.memeapp.memeflex.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.memeapp.memeflex.ui.auth.ForgotPasswordScreen
import com.memeapp.memeflex.ui.auth.LoginScreen
import com.memeapp.memeflex.ui.auth.RegisterScreen
import com.memeapp.memeflex.ui.search.SearchScreen
import com.memeapp.memeflex.ui.feed.FeedScreen
import com.memeapp.memeflex.ui.post.PostMemeScreen
import com.memeapp.memeflex.ui.profile.ProfileScreen
import com.memeapp.memeflex.ui.splash.SplashScreen

@Composable
fun MemeFlexNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.SPLASH
) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        contentWindowInsets = WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal)
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Splash Screen
            composable(Routes.SPLASH) {
                SplashScreen(
                    onSplashFinished = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.SPLASH) { inclusive = true }
                        }
                    }
                )
            }

            // Authentication Flow
            composable(Routes.LOGIN) {
                LoginScreen(
                    onSignUp = { navController.navigate(Routes.SIGNUP) },
                    onForgotPassword = { navController.navigate(Routes.FORGOT_PASSWORD) },
                    onLogin = { username, password ->
                        navController.navigate(Routes.FEED) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }}
                )
            }

            composable(Routes.SIGNUP) {
                RegisterScreen(
                    onLogin = { navController.navigate(Routes.LOGIN) },
                    onRegister = { email, password ->
                        navController.navigate(Routes.FEED) {
                        popUpTo(Routes.SIGNUP) { inclusive = true }
                    }}
                )
            }

            composable(Routes.FORGOT_PASSWORD) {
                ForgotPasswordScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onPasswordResetSuccess = { navController.popBackStack() }
                )
            }

            // Main App Screens
            composable(Routes.FEED) {
                FeedScreen()
            }

            composable(Routes.SEARCH) {
                SearchScreen()
            }

            composable(Routes.PROFILE) {
                ProfileScreen(
                    onLogout = { navController.navigate(Routes.LOGIN) {
                        popUpTo(0)
                    }}
                )
            }

            composable(Routes.POST_MEME) {
                PostMemeScreen(
                    onBackPressed = { navController.popBackStack() },
                    onPostClicked = { navController.popBackStack() }
                )
            }
        }
    }
}