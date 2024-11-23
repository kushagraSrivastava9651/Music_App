package com.example.musicappui.Login_data

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.musicappui.ui.MainView

@Composable
fun NavigationGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.SignupScreen.route
    ) {
        composable(Screen.SignupScreen.route) {
            SignUpScreen(authViewModel = authViewModel,
                onNavigateToLogin = { navController.navigate(Screen.LoginScreen.route) }
            )
        }
        composable(Screen.LoginScreen.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToSignUp = { navController.navigate(Screen.SignupScreen.route) },
                onSignInSuccess =  {
                    navController.navigate(Screen.MainView.route) {
                        popUpTo(Screen.SignupScreen.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.MainView.route) {
            MainView(navController = navController)
        }


    }

}

 