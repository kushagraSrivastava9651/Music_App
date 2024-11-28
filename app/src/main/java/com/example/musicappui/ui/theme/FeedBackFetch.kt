package com.example.musicappui.ui.theme

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun FeedbackListScreen(navController: NavController) {
    val context = LocalContext.current
    val feedbackList = remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    val currentUser = FirebaseAuth.getInstance().currentUser

    if (currentUser == null) {
        Toast.makeText(context, "You must be logged in to view feedbacks.", Toast.LENGTH_SHORT).show()
        navController.navigateUp()
        return
    }

    // Fetch feedbacks when screen loads
    LaunchedEffect(Unit) {
        fetchAllFeedbacks(
            userId = currentUser.uid,
            onSuccess = { feedbacks ->
                feedbackList.value = feedbacks
            },
            onFailure = { exception ->
                Toast.makeText(context, "Error fetching feedbacks: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Feedbacks", color = Color.White) },
                backgroundColor = Color(0xFF4A90E2),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(feedbackList.value) { feedback ->
                FeedbackCard(
                    feedback = feedback,
                    onDelete = { feedbackId ->
                        deleteFeedback(
                            userId = currentUser.uid,
                            feedbackId = feedbackId,
                            onSuccess = {
                                Toast.makeText(context, "Feedback deleted successfully!", Toast.LENGTH_SHORT).show()
                                feedbackList.value = feedbackList.value.filter { it["id"] != feedbackId }
                            },
                            onFailure = { exception ->
                                Toast.makeText(context, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun FeedbackCard(
    feedback: Map<String, Any>,
    onDelete: (String) -> Unit
) {
    val selectedOption = feedback["selectedOption"] as? String ?: "N/A"
    val feedbackText = feedback["feedbackText"] as? String ?: "No feedback provided"
    val timestamp = feedback["timestamp"] as? Timestamp
    val date = timestamp?.toDate()?.toString() ?: "Unknown date"
    val feedbackId = feedback["id"] as? String ?: ""

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = 8.dp,
        shape = RoundedCornerShape(12.dp),
        backgroundColor = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Selected Option:",
                    style = MaterialTheme.typography.subtitle2,
                    color = Color(0xFF4A90E2)
                )
                IconButton(onClick = { onDelete(feedbackId) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Feedback",
                        tint = Color.Red
                    )
                }
            }
            Text(
                text = selectedOption,
                style = MaterialTheme.typography.body1,
                color = Color.Black,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )

            Divider(color = Color(0xFFBDBDBD), thickness = 1.dp)

            Text(
                text = "Feedback:",
                style = MaterialTheme.typography.subtitle2,
                color = Color(0xFF4A90E2),
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = feedbackText,
                style = MaterialTheme.typography.body1,
                color = Color.Black,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )

            Divider(color = Color(0xFFBDBDBD), thickness = 1.dp)

            Text(
                text = "Submitted on:",
                style = MaterialTheme.typography.subtitle2,
                color = Color(0xFF4A90E2),
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = date,
                style = MaterialTheme.typography.caption,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}



fun fetchAllFeedbacks(
    userId: String,
    onSuccess: (List<Map<String, Any>>) -> Unit,
    onFailure: (Exception) -> Unit
) {
    val db = FirebaseFirestore.getInstance()

    db.collection("users")
        .document(userId)
        .collection("feedback")
        .get()
        .addOnSuccessListener { querySnapshot ->
            val feedbacks = querySnapshot.documents.map { document ->
                val data = document.data ?: emptyMap()
                data + ("id" to document.id) // Add document ID for delete operation
            }
            onSuccess(feedbacks)
        }
        .addOnFailureListener { exception ->
            onFailure(exception)
        }
}
fun deleteFeedback(
    userId: String,
    feedbackId: String,
    onSuccess: () -> Unit,
    onFailure: (Exception) -> Unit
) {
    val db = FirebaseFirestore.getInstance()

    db.collection("users")
        .document(userId)
        .collection("feedback")
        .document(feedbackId)
        .delete()
        .addOnSuccessListener {
            onSuccess()
        }
        .addOnFailureListener { exception ->
            onFailure(exception)
        }
}
