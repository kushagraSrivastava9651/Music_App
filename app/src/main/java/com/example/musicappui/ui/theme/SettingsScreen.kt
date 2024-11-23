package com.example.musicappui.ui.theme
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.material.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(
    darkThemeSwitchState: MutableState<Boolean>,
    notificationSwitchState: MutableState<Boolean>,
    applyTheme: (Boolean) -> Unit
) {
    // Define a custom color palette
    val customLightColors = lightColors(
        primary = Color(0xFF4A90E2), // Muted Blue
        primaryVariant = Color(0xFF3B79C4), // Darker Muted Blue
        secondary = Color(0xFF50BFA0), // Muted Green
        secondaryVariant = Color(0xFF3C9A81), // Darker Muted Green
        surface = Color.White,
        onSurface = Color.DarkGray,
        background = Color(0xFFF2F2F2), // Light gray for background
        onBackground = Color.DarkGray
    )

    val customDarkColors = darkColors(
        primary = Color(0xFF4A90E2), // Muted Blue
        primaryVariant = Color(0xFF3B79C4), // Darker Muted Blue
        secondary = Color(0xFF50BFA0), // Muted Green
        secondaryVariant = Color(0xFF3C9A81), // Darker Muted Green
        surface = Color.DarkGray,
        onSurface = Color.White,
        background = Color.Black,
        onBackground = Color.White
    )

    // Apply theme dynamically based on the dark theme switch
    val currentColors = if (darkThemeSwitchState.value) customDarkColors else customLightColors

    MaterialTheme(
        colors = currentColors,
        typography = Typography(defaultFontFamily = FontFamily.SansSerif)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Settings",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary
                        ) },
                    backgroundColor = MaterialTheme.colors.surface,
                    contentColor = MaterialTheme.colors.onSurface,
                    elevation = 4.dp
                )
            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // Dark Theme Toggle
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            checked = darkThemeSwitchState.value,
                            onCheckedChange = {
                                darkThemeSwitchState.value = it
                                applyTheme(it)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colors.secondary,
                                checkedTrackColor = MaterialTheme.colors.secondaryVariant
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Dark Theme",
                            fontSize = 20.sp,
                            color = MaterialTheme.colors.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Notification Toggle
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Switch(
                            checked = notificationSwitchState.value,
                            onCheckedChange = { isChecked ->
                                notificationSwitchState.value = isChecked
                               // Call the function here after updating the state
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colors.secondary,
                                checkedTrackColor = MaterialTheme.colors.secondaryVariant
                            )
                        )
                        handleNotifications(notificationSwitchState.value)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Notifications",
                            fontSize = 20.sp,
                            color = MaterialTheme.colors.onSurface
                        )
                    }
                }
            }
        )
    }
}


@Composable
fun handleNotifications(isChecked: Boolean):Unit {
    val context = LocalContext.current
    val message = if (isChecked) "Notifications enabled" else "Notifications disabled"
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
