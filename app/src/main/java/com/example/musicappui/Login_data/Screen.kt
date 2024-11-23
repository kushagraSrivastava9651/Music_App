package com.example.musicappui.Login_data

import androidx.annotation.DrawableRes
import com.example.musicappui.R

sealed class Screen(val route:String) {
    object LoginScreen : Screen("loginscreen")
    object SignupScreen : Screen("signupscreen")
    object MainView : Screen("main_screen")
}
