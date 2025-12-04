package com.tanveer.focusflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tanveer.focusflow.auth.AuthScreens.ForgotPasswordScreen
import com.tanveer.focusflow.auth.AuthScreens.LoginScreen
import com.tanveer.focusflow.auth.AuthScreens.RegisterScreen
import com.tanveer.focusflow.auth.AuthViewModel
import com.tanveer.focusflow.ui.navigation.MainNavGraph
import com.tanveer.focusflow.ui.theme.FocusFlowTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FocusFlowTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel()

                NavHost(
                    navController = navController,
                    startDestination = if (authViewModel.repo.getCurrentUser() != null) "main" else "login"
                ) {
                    // Login
                    composable("login") {
                        LoginScreen(
                            onLogin = { email, pass ->
                                authViewModel.login(email, pass) {
                                    navController.navigate("main") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            },
                            onRegisterClick = { navController.navigate("register") },
                            onForgotClick = { navController.navigate("forgot") }
                        )
                    }

                    // Register
                    composable("register") {
                        RegisterScreen(
                            onRegister = { name, email, pass ->
                                authViewModel.register(name, email, pass) {
                                    navController.navigate("main") {
                                        popUpTo("register") { inclusive = true }
                                    }
                                }
                            },
                            onLoginClick = { navController.navigate("login") }
                        )
                    }

                    // Forgot Password
                    composable("forgot") {
                        ForgotPasswordScreen(
                            onReset = { email ->
                                authViewModel.forgotPassword(email)
                                navController.navigate("login") {
                                    popUpTo("forgot") { inclusive = true }
                                }
                            },
                            onBackToLogin = { navController.navigate("login") }
                        )
                    }

                    // Main app screens
                    composable("main") { MainNavGraph(navController) }
                }

            }
        }
    }
}




