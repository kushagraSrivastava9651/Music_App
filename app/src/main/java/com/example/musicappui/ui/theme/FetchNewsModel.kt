package com.example.musicappui.ui.theme

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import androidx.compose.runtime.State

class FetchNewsViewModel : ViewModel() {

    private val _categoriesState = mutableStateOf(RecipeState())
    val categoriesState: State<RecipeState> = _categoriesState

    // Firebase Realtime Database reference
    private val databaseReference = FirebaseDatabase.getInstance().getReference("categories")

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        _categoriesState.value = RecipeState(loading = true)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val categories = mutableListOf<Item>()
                    for (categorySnapshot in snapshot.children) {
                        val item = categorySnapshot.getValue(Item::class.java)
                        if (item != null) {
                            categories.add(item)
                        }
                    }
                    _categoriesState.value = RecipeState(list = categories, loading = false)
                } catch (e: Exception) {
                    _categoriesState.value = RecipeState(error = "Error parsing data: ${e.message}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _categoriesState.value = RecipeState(error = "Database error: ${error.message}")
            }
        })
    }

    data class RecipeState(
        val loading: Boolean = true,
        var list: List<Item> = emptyList(),
        val error: String? = null
    )
}







/*
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicappui.apiService
import kotlinx.coroutines.launch

class FetchNewsViewModel : ViewModel() {

    private val _categoriesState = mutableStateOf(RecipeState())
    val categoriesState: State<RecipeState> = _categoriesState

    init {
        fetchCategories()
    }

    private fun fetchCategories() {

        viewModelScope.launch {
            try {
                _categoriesState.value = RecipeState(loading = true)
                val response = apiService.getCategories()
                val categories = response.categories
                // Simulate fetching data from API (replace with actual API call)
             /*   val categories = listOf(
                    Item(1, "Breaking News: COVID-19 Vaccine Rollout Begins", "Description 1", "https://i.imgur.com/7OhMHE8.png"),
                    Item(2, "Tech Giant Unveils Revolutionary AI Technology", "Description 2", "https://i.imgur.com/7OhMHE8.png"),
                    Item(3, "Climate Change Report: Urgent Action Needed", "Description 3", "https://i.imgur.com/7OhMHE8.png"),
                    // Add more items as needed
                )

              */
                _categoriesState.value = RecipeState(list = categories, loading = false)
            } catch (e: Exception) {
                _categoriesState.value = RecipeState(error = "Error fetching data: ${e.message}")
            }
        }
    }

    data class RecipeState(
        val loading: Boolean = true,
        var list: List<Item> = emptyList(),
        val error: String? = null
    )
}


 */