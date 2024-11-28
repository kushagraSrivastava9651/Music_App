package com.example.musicappui.chat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.launch
// Content.kt



import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun Content(navController: NavController, viewModel: ChatViewModel = viewModel()) {
    val appUiState by viewModel.uiState.collectAsState()
    val imageRequestBuilder = ImageRequest.Builder(LocalContext.current)
    val imageLoader = ImageLoader.Builder(LocalContext.current).build()

    val coroutineScope = rememberCoroutineScope()
    ChatScreen(navController, uiState = appUiState) { inputText, selectedItem ->
        coroutineScope.launch {
            try {
                val bitmaps = selectedItem.mapNotNull {
                    val imageRequest = imageRequestBuilder.data(it).size(768).build()
                    val imageResult = imageLoader.execute(imageRequest)
                    if (imageResult is SuccessResult) {
                        (imageResult.drawable as? BitmapDrawable)?.bitmap
                    } else {
                        null
                    }
                }
                viewModel.questioning(userInput = inputText, selectedImage = bitmaps)
            } catch (e: Exception) {
                Log.e("Content", "Error processing images", e)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    uiState: chatUiState,
    onSendClick: (String, List<Uri>) -> Unit
) {
    val backgroundColor = Color(0xFFFAFAFA) // White background
    val primaryColor = Color(0xFF4A90E2) // Muted Blue
    val secondaryColor = Color(0xFF34C759) // Muted Green
    val textColor = Color.Black

    var userQues by rememberSaveable { mutableStateOf("") }
    val imageUris = rememberSaveable(saver = UriCustomSaver()) { mutableStateListOf<Uri>() }
    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { imageUris.add(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI ChatBot", color = textColor) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = textColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(  primaryColor)
            )
        },
        bottomBar = {
            Column(
                Modifier
                    .background(backgroundColor)
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(Icons.Default.AddCircle, contentDescription = "Add Image", tint = secondaryColor)
                    }
                    OutlinedTextField(
                        value = userQues,
                        onValueChange = { userQues = it },
                        label = { Text("Type your question", color = textColor) },
                        placeholder = { Text("Upload images and ask questions", color = textColor.copy(alpha = 0.6f)) },
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = textColor,
                            placeholderColor = textColor.copy(alpha = 0.6f),
                            focusedBorderColor = primaryColor,
                            unfocusedBorderColor = primaryColor,
                            focusedLabelColor = textColor,
                            unfocusedLabelColor = textColor,
                             backgroundColor = backgroundColor
                        )
                    )
                    IconButton(
                        onClick = {
                            if (userQues.isNotBlank()) {
                                onSendClick(userQues, imageUris)
                            }
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send", tint = secondaryColor)
                    }
                }
                AnimatedVisibility(visible = imageUris.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(  backgroundColor)
                    ) {
                        LazyRow(
                            modifier = Modifier.padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(imageUris) { imageUri ->
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    AsyncImage(
                                        model = imageUri,
                                        contentDescription = "Selected Image",
                                        modifier = Modifier
                                            .size(50.dp)
                                            .background(primaryColor.copy(alpha = 0.1f), shape = MaterialTheme.shapes.small)
                                            .padding(4.dp)
                                    )
                                    TextButton(onClick = { imageUris.remove(imageUri) }) {
                                        Text("Remove", color = Color.Red)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            when (uiState) {
                is chatUiState.Initial -> {
                    Text(
                        text = "Start your conversation!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                is chatUiState.Loading -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(color = primaryColor)
                    }
                }
                is chatUiState.Success -> {
                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors( secondaryColor.copy(alpha = 0.1f))
                    ) {
                        Text(
                            text = (uiState as chatUiState.Success).outputText,
                            style = MaterialTheme.typography.bodyLarge,
                            color = textColor,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                is chatUiState.Error -> {
                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors( Color.Red.copy(alpha = 0.1f))
                    ) {
                        Text(
                            text = (uiState as chatUiState.Error).error,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Red,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                else -> {}
            }
        }
    }
}
