package com.example.musicappui

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.musicappui.Login_data.AuthViewModel
import com.example.musicappui.Login_data.NavigationGraph

import com.example.musicappui.ui.MainView
import com.example.musicappui.ui.Navigation

import com.example.musicappui.ui.theme.MusicAppUITheme
import com.example.musicappui.ui.theme.MyThemedApp
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability


import org.json.JSONObject

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val authViewModel: AuthViewModel = viewModel()

            val context = LocalContext.current

            // Check user's preference for dark mode
            val isDarkModeEnabled = getDarkModePreference(context)


            MyThemedApp(isDarkModeEnabled) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
               //     if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context) != ConnectionResult.SUCCESS) {
                 //       Log.e("GooglePlayServices", "Google Play Services are not available.")
                   // }

                    NavigationGraph(navController = navController, authViewModel = authViewModel)
                }
            }

        }
    }

    private fun getDarkModePreference(context: Context): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getBoolean("dark_mode_enabled", false)
    }

}