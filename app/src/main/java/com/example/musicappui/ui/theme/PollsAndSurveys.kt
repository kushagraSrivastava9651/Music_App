package com.example.musicappui.ui.theme

 import android.util.Log
 import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
 import androidx.compose.material.Icon
 import androidx.compose.material.IconButton
 import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
 import androidx.compose.material.Scaffold
 import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
 import androidx.compose.material.TopAppBar
 import androidx.compose.material.icons.Icons
 import androidx.compose.material.icons.filled.ArrowBack
 import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore



import com.google.firebase.Timestamp

import com.google.firebase.auth.FirebaseAuth
 import com.google.firebase.firestore.FieldValue

@Composable
fun PollSurveyScreen(navController: NavController) {
    val selectedOption = remember { mutableStateOf("") }
    val feedbackText = remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Poll/Survey", color = Color.White)
                },
                backgroundColor = Color(0xFF4A90E2), // Replace `primaryColor` with actual color
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                elevation = 4.dp
            )
        },
        backgroundColor = Color(0xFFFAFAFA) // Replace `backgroundColor` with actual color
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Ensure padding accounts for the scaffold
                .padding(16.dp), // Additional inner padding
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {


            // Survey Question
            Text(
                text = "How likely are you to share news from this app?",
                color = Color.Black,
                style = MaterialTheme.typography.subtitle1
            )

            // Options List
            listOf("Very Likely", "Somewhat Likely", "Neutral","Unlikely", "Never").forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedOption.value = option },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedOption.value == option,
                        onClick = { selectedOption.value = option },
                        colors = RadioButtonDefaults.colors(Color(0xFF4A90E2))
                    )
                    Text(
                        text = option,
                        color = Color.Black,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            // Feedback Input Field
            OutlinedTextField(
                value = feedbackText.value,
                onValueChange = { feedbackText.value = it },
                label = { Text("Additional Feedback", color = Color(0xFF4A90E2)) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF4A90E2),
                    unfocusedBorderColor = Color(0xFF34C759),
                    textColor = Color.Black
                )
            )

            // Submit Button
            Button(
                onClick = {
                    val currentUser = FirebaseAuth.getInstance().currentUser

                    if (currentUser == null) {
                        Toast.makeText(
                            context,
                            "You must be logged in to submit feedback.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    val userId = currentUser.uid
                    val feedbackData = hashMapOf(
                        "selectedOption" to selectedOption.value,
                        "feedbackText" to feedbackText.value,
                        "timestamp" to FieldValue.serverTimestamp()
                    )

                    addFeedbackToFirestore(
                        userId = userId,
                        feedbackData = feedbackData,
                        onSuccess = {
                            Toast.makeText(
                                context,
                                "Feedback submitted successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.navigateUp() // Navigate back on success
                        },
                        onFailure = { exception ->
                            Toast.makeText(
                                context,
                                "Error: ${exception.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4A90E2))
            ) {
                Text(text = "Submit", color = Color.White)
            }
        }
    }
}


    fun addFeedbackToFirestore(
        userId: String,
        feedbackData: Map<String, Any>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        // Firebase Firestore instance
        val db = FirebaseFirestore.getInstance()

        // Add feedback to the `feedback` subcollection of the specific user
        db.collection("users")
            .document(userId)
            .collection("feedback")
            .add(feedbackData)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
