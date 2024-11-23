package com.example.musicappui.ui.theme


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.musicappui.Login_data.AuthViewModel
import com.example.musicappui.R
@Composable
fun AccountView(viewModel: AuthViewModel) {
    val userDetails by viewModel.userDetails.observeAsState()

    // Define the colors
    val white = Color(0xFFFFFFFF)
    val darkGrey = Color(0xFF212121)
    val mutedBlue = Color(0xFF607D8B)
    val mutedGreen = Color(0xFF80C8A7)

    val backgroundColor = white // Main background color
    val primaryColor = mutedBlue // Primary accent color (Muted Blue)
    val secondaryColor = mutedGreen // Secondary accent color (Muted Green)

    LaunchedEffect(Unit) {
        viewModel.getCurrentUser()?.let {
            viewModel.fetchUserDetails(it.uid)
        }
    }

    when (val result = userDetails) {
        is com.example.musicappui.Login_data.Result.Success -> {
            val userMap = result.data
            val firstName = userMap["firstName"] as? String ?: ""
            val lastName = userMap["lastName"] as? String ?: ""
            val email = userMap["email"] as? String ?: ""

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(white) // White background
                    .padding(16.dp)
            ) {
                // Header Row: User information and Account icon
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            primaryColor.copy(alpha = 0.1f), // Muted Blue background with some transparency
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Account",
                            tint = primaryColor,
                            modifier = Modifier
                                .size(40.dp)
                                .padding(end = 8.dp)
                        )
                        Column {
                            Text(
                                "$firstName $lastName",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = darkGrey // Dark Grey for name
                                )
                            )
                            Text(
                                text = extractUserHandleFromEmail(email),
                                style = MaterialTheme.typography.bodySmall.copy(color = primaryColor)
                            )
                        }
                    }
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = primaryColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Quick News Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            secondaryColor.copy(alpha = 0.1f), // Muted Green background with transparency
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(16.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_library_music_24),
                        contentDescription = "Quick News",
                        tint = primaryColor,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(end = 8.dp)
                    )
                    Text(
                        "Quick News",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium,
                            color = darkGrey // Dark Grey text for a professional look
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Divider between sections
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(primaryColor) // Muted Blue divider
                )
            }
        }
        is com.example.musicappui.Login_data.Result.Error -> {
            Text("Error fetching user details: ${result.exception.message}", color = MaterialTheme.colorScheme.error)
        }
        null -> {
            // Display a loading indicator
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = primaryColor)
            }
        }
    }
}

fun extractUserHandleFromEmail(email: String): String {
    return email.substringBefore("@")
}

@Preview(showBackground = true)
@Composable
fun AccountViewPreview() {
    MaterialTheme {
        AccountView(viewModel = AuthViewModel())
    }
}
