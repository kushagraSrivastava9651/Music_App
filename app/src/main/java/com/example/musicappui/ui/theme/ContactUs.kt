package com.example.musicappui.ui.theme
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


import android.content.Context
import android.database.Cursor
 import android.provider.OpenableColumns
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

fun getFileName(context: Context, uri: Uri): String? {
    var fileName: String? = null
    val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            fileName = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
        }
    }
    return fileName
}



// Colors
val backgroundColor = Color(0xFFFAFAFA) // White background
val primaryColor = Color(0xFF4A90E2) // Muted Blue
val secondaryColor = Color(0xFF34C759) // Muted Green
val textColor = Color.Black
@Composable
fun ContactScreen(navController: NavController) {
    val context = LocalContext.current

    // State variables
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    var selectedFileUri by remember { mutableStateOf<Uri?>(null) } // To hold the selected file URI

    // Launcher for opening file picker
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedFileUri = uri
            Toast.makeText(context, "File Selected: ${uri.path}", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "No file selected", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contact Us", color = textColor) },
                backgroundColor = backgroundColor,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Person, contentDescription = "Back")
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Header Section
            Text(
                text = "QUICK CONTACT",
                color = textColor,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Form Fields
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
            )

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Phone") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
            )

            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Message") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(bottom = 16.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
            )

            // Buttons Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { filePickerLauncher.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(backgroundColor = secondaryColor),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                ) {
                    Text("Choose File", color = Color.White)
                }

                Button(
                    onClick = {
                        if (email.isNotEmpty() && message.isNotEmpty() && selectedFileUri != null) {
                            sendEmail(
                                context = context,
                                email = "kushagra3946@gmail.com",
                                subject = "Quick Contact: $name",
                                message = "Phone: $phoneNumber\n\n$message",
                                attachmentUri = selectedFileUri
                            )
                        } else {
                            Toast.makeText(context, "Please fill all required fields and choose a file.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = primaryColor),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                ) {
                    Text("Send", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))



            // Show selected file name
            if (selectedFileUri != null) {
                Text(
                    text = "Selected File: ${selectedFileUri?.path}",
                    color = textColor,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(32.dp))

            // Contact Details Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color.LightGray.copy(alpha = 0.1f),
                        androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Ajay Kumar Garg Engineering College,\nGhaziabad, 201009, Ghaziabad, INDIA.",
                    color = textColor,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "+91-80097XXXX58",
                    color = textColor,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "kushagra3946@gmail.com",
                    color = textColor,
                    fontSize = 14.sp
                )
            }
        }
    }
}

fun sendEmail(
    context: android.content.Context,
    email: String,
    subject: String,
    message: String,
    attachmentUri: Uri?
) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/*" // Specify the file type (image in this case)
        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, message)

        attachmentUri?.let {
            putExtra(Intent.EXTRA_STREAM, it) // Attach the image file URI
        }
    }
    try {
        context.startActivity(Intent.createChooser(intent, "Send Email"))
    } catch (e: Exception) {
        Toast.makeText(context, "No email client found.", Toast.LENGTH_SHORT).show()
    }
}
