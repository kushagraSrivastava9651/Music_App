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
    val backgroundColor = MaterialTheme.colorScheme.background
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
                title = { Text("AI ChatBot", color = MaterialTheme.colorScheme.onPrimary) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
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
                        Icon(Icons.Default.AddCircle, contentDescription = "Add Image", tint = MaterialTheme.colorScheme.primary)
                    }
                    OutlinedTextField(
                        value = userQues,
                        onValueChange = { userQues = it },
                        label = { Text("User Input") },
                        placeholder = { Text("Upload Image And Ask Question") },
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            textColor = Color.Gray,
                            placeholderColor = Color.Gray,
                            cursorColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            unfocusedLabelColor = Color.Black
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
                        Icon(Icons.Default.Send, contentDescription = "Send", tint = MaterialTheme.colorScheme.primary)
                    }
                }
                AnimatedVisibility(visible = imageUris.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
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
                                        contentDescription = "",
                                        modifier = Modifier
                                            .size(50.dp)
                                            .background(Color.LightGray, shape = MaterialTheme.shapes.small)
                                            .padding(4.dp)
                                    )
                                    TextButton(onClick = { imageUris.remove(imageUri) }) {
                                        Text("Remove", color = MaterialTheme.colorScheme.error)
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
                    // Display initial state UI
                    Text(
                        text = "Start your conversation!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                is chatUiState.Loading -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
                is chatUiState.Success -> {
                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Text(
                            text = (uiState as chatUiState.Success).outputText,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                is chatUiState.Error -> {
                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Text(
                            text = (uiState as chatUiState.Error).error,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onError,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                else -> {}
            }
        }
    }
}
