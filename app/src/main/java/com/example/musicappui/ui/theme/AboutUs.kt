package com.example.musicappui.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun AboutUsScreen(navController: NavController) {
    // Colors
    val backgroundColor = Color(0xFFF5F5F5) // Light gray
    val primaryColor = Color(0xFF4A90E2) // Muted Blue
    val textColor = Color.Black
    val cardBackground = Color.White

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("About Us", color = Color.White)
                },
                backgroundColor = primaryColor,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                elevation = 4.dp
            )
        },
        backgroundColor = backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Logo or Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(primaryColor, shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Quick News",
                    fontSize = 28.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mission Section
            CardSection(
                title = "Our Mission",
                description = "At Quick News, our mission is to provide real-time, reliable, and personalized news updates to users. " +
                        "We aim to empower people with accurate information and keep them informed about the latest events globally. " +
                        "Our app uses cutting-edge AI technology to deliver news tailored to your interests, ensuring accessibility, convenience, and trustworthiness.",
                textColor = textColor,
                cardBackground = cardBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Developed By Section
            CardSection(
                title = "Developed By",
                description = "This app was developed by Kushagra Srivastava, a dedicated and passionate student from Ajay Kumar Garg Engineering College (AKGEC), pursuing IT branch. " +
                        "With expertise in Android development, this project represents his commitment to innovation and user-centric design.",
                textColor = textColor,
                cardBackground = cardBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Contact Section
            CardSection(
                title = "Contact Us",
                description = "For queries or feedback, reach out at kushagra9651@gmail.com",
                textColor = textColor,
                cardBackground = cardBackground
            )
        }
    }
}

// Reusable Composable for Card Sections
@Composable
fun CardSection(
    title: String,
    description: String,
    textColor: Color,
    cardBackground: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(12.dp),
        backgroundColor = cardBackground
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}
