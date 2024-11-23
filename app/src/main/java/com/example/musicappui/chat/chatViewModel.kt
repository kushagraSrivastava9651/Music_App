package com.example.musicappui.chat

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.rememberNavController
import com.example.musicappui.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel

import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
// ChatViewModel.kt
// ChatViewModel.kt
class ChatViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<chatUiState> = MutableStateFlow(chatUiState.Initial)
    val uiState: StateFlow<chatUiState> = _uiState

    private var generativeModel: GenerativeModel

    init {
        val config = generationConfig {
            temperature = 0.7f
        }

        generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = BuildConfig.API_KEY // This should now resolve correctly
        )
    }

    fun questioning(userInput: String, selectedImage: List<Bitmap>) {
        _uiState.value = chatUiState.Loading
        val prompt = "Take a look at image, and then answer the following question: $userInput"

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val content = content {
                    for (bitmap in selectedImage) {
                        image(bitmap)
                    }
                    text(prompt)
                }
                var output = ""
                generativeModel.generateContentStream(content).collect {
                    output += it.text
                    _uiState.value = chatUiState.Success(output)
                }
            } catch (e: Exception) {
                _uiState.value = chatUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}

                sealed class chatUiState {
                    object Initial : chatUiState()
                    object Loading : chatUiState()
                    data class Success(val outputText: String) : chatUiState()
                    data class Error(val error: String) : chatUiState()
                }