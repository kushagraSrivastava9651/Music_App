package com.example.musicappui.ui.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.musicappui.R
import kotlin.text.CharCategory


/*
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeView(navController: NavController) {
    val recipeViewModel: FetchNewsViewModel = viewModel()
    val viewState by recipeViewModel.categoriesState
    val backgroundColor = MaterialTheme.colors.background
    val searchQuery = rememberSaveable { mutableStateOf("") }
    val isSearchBarVisible = rememberSaveable { mutableStateOf(false) }

    val categories = listOf(
        "Live News",
        "New Release",
        "Favorites",
        "Top Rated"
    )

    val shuffledLists = remember {
        mutableStateOf<Map<String, List<Item>>>(emptyMap())
    }

    if (shuffledLists.value.isEmpty() && viewState.list.isNotEmpty()) {
        val shuffledMap = categories.associateWith { category ->
            viewState.list.shuffled().take(6)
        }
        shuffledLists.value = shuffledMap
    }

    val filteredLists = remember(searchQuery.value, shuffledLists.value) {
        if (searchQuery.value.isEmpty()) {
            shuffledLists.value
        } else {
            shuffledLists.value.mapValues { (category, items) ->
                items.filter { it.title.contains(searchQuery.value, ignoreCase = true) }
            }
        }
    }

    Column {
        TopAppBar(
            backgroundColor = ColorRepository.ForestGreen,
            contentColor = Color.Black,
            elevation = 0.dp,
            title = {
                if (isSearchBarVisible.value) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        TextField(
                            value = searchQuery.value,
                            onValueChange = { searchQuery.value = it },
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    color = MaterialTheme.colors.surface,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            placeholder = { Text("Search...", color = Color.Gray) },
                            singleLine = true,
                            trailingIcon = {
                                if (searchQuery.value.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery.value = "" }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_clear_24),
                                            contentDescription = "Clear",
                                            tint = Color.Gray
                                        )
                                    }
                                }
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = MaterialTheme.colors.surface,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                else{
                Text(
                    text = "Search Option",
                    style = MaterialTheme.typography.h6.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    modifier = Modifier.weight(1f)
                )
            }},
            actions = {
                if (!isSearchBarVisible.value) {
                    IconButton(
                        onClick = { isSearchBarVisible.value = !isSearchBarVisible.value },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_search_24),
                            contentDescription = "Search",
                            tint = Color.Black
                        )
                    }
                }else{
                    IconButton(
                        onClick = { isSearchBarVisible.value = !isSearchBarVisible.value },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_close_24),
                            contentDescription = "Close",
                            tint = Color.Black
                        )
                    }
                }
            },
            modifier = Modifier.background(
                Brush.horizontalGradient(
                    listOf(
                        ColorRepository.YellowGreen,
                        ColorRepository.Olive,
                        ColorRepository.ForestGreen
                    ),
                    startX = 0f,
                    endX = 1000f
                )
            )
        )



        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            when {
                viewState.loading -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp),
                            color = ColorRepository.ForestGreen
                        )
                        Text(
                            text = "Loading...",
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }

                viewState.error != null -> {
                    Text(
                        text = "Error Occurred: ${viewState.error}",
                        color = ColorRepository.Red,
                        style = MaterialTheme.typography.body1
                    )
                }

                else -> {
                    LazyColumn {
                        categories.forEach { category ->
                            val itemsForCategory = filteredLists[category] ?: emptyList()
                            if (itemsForCategory.isNotEmpty()) {
                                stickyHeader {
                                    Text(
                                        text = category,
                                        modifier = Modifier
                                            .background(Color.White)
                                            .padding(16.dp),
                                        color = Color.Black,
                                        style = MaterialTheme.typography.h6.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp
                                        )
                                    )
                                }
                                item {
                                    LazyRow {
                                        items(itemsForCategory) { item ->
                                            BrowserItem(
                                                item = item,
                                                drawable = item.image,
                                                onItemClick = {
                                                    navController.navigate("details/${item.id}")
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BrowserItem(item: Item, drawable: String, onItemClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .size(200.dp)
            .border(1.dp, ColorRepository.Gray, RoundedCornerShape(12.dp)),
        onClick = onItemClick,
        elevation = 8.dp,
        shape = RoundedCornerShape(12.dp),
        backgroundColor = Color.White
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.subtitle1.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                modifier = Modifier.padding(bottom = 8.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(ColorRepository.LightGray, ColorRepository.Gray)
                        )
                    )
            ) {
                val painter = rememberAsyncImagePainter(model = item.image)
                Image(
                    painter = painter,
                    contentDescription = item.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                if (painter.state is AsyncImagePainter.State.Error) {
                    Icon(
                        painter = painterResource(id = R.drawable.error),
                        contentDescription = "Error",
                        tint = ColorRepository.Red,
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
}*/

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun HomeView(navController: NavController) {
    val recipeViewModel: FetchNewsViewModel = viewModel()
    val viewState by recipeViewModel.categoriesState
    val searchQuery = rememberSaveable { mutableStateOf("") }
    val isSearchBarVisible = rememberSaveable { mutableStateOf(false) }

    val categories = listOf(
        "Live News",
        "New Release",
        "Favorites",
        "Top Rated"
    )

    val filters = remember {
        mutableStateOf(FilterState()) // To hold filter states
    }

    // Prepare shuffled lists for display
    val shuffledLists = remember {
        mutableStateOf<Map<String, List<Item>>>(emptyMap())
    }
    if (shuffledLists.value.isEmpty() && viewState.list.isNotEmpty()) {
        shuffledLists.value = categories.associateWith { category ->
            viewState.list.shuffled().take(6)
        }
    }

    // Filter lists based on search query and filters
    val filteredLists = remember(searchQuery.value, filters.value, viewState.list) {
        if (searchQuery.value.isEmpty() && filters.value.isEmpty()) {
            shuffledLists.value
        } else {
            categories.associateWith { category ->
                viewState.list.filter { item ->
                    (searchQuery.value.isEmpty() || item.title.contains(searchQuery.value, ignoreCase = true)) &&
                            (filters.value.category.isEmpty() || item.description == filters.value.category) &&
                            (filters.value.language.isEmpty() || item.description == filters.value.language)
                }
            }
        }
    }

    Column {
        // Top bar with search and filters
        UnifiedTopBar(
            searchQuery = searchQuery,
            isSearchBarVisible = isSearchBarVisible,
            filters = filters,
            onToggleSearch = { isSearchBarVisible.value = !isSearchBarVisible.value }
        )

        // Main content box
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            when {
                viewState.loading -> LoadingView()
                viewState.error != null -> ErrorView(errorMessage = viewState.error)
                else -> {
                    LazyColumn {
                        categories.forEach { category ->
                            val itemsForCategory = filteredLists[category] ?: emptyList()
                            if (itemsForCategory.isNotEmpty()) {
                                stickyHeader {
                                    CategoryHeader(category = category)
                                }
                                item {
                                    CategoryRow(
                                        items = itemsForCategory,
                                        navController = navController
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



data class FilterState(
    val category: String = "",
    val language: String = ""
) {
    fun isEmpty(): Boolean = category.isEmpty() && language.isEmpty()
}
@Composable
fun EnhancedSearchBar(
    searchQuery: MutableState<String>,
    isSearchBarVisible: MutableState<Boolean>,
    onToggleSearch: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp))
            .background(MaterialTheme.colors.surface, RoundedCornerShape(12.dp))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_search_24),
                contentDescription = "Search Icon",
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            TextField(
                value = searchQuery.value,
                onValueChange = { searchQuery.value = it },
                placeholder = { Text("Search news, categories...", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                trailingIcon = {
                    if (searchQuery.value.isNotEmpty()) {
                        IconButton(onClick = { searchQuery.value = "" }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_clear_24),
                                contentDescription = "Clear",
                                tint = Color.Gray
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun EnhancedFilterPanel(filters: MutableState<FilterState>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp)),
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Filters",
                style = MaterialTheme.typography.subtitle1.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onSurface
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                DropdownMenuWithLabel(
                    label = "Category",
                    options = listOf("Live News", "New Release", "Favorites", "Top Rated"),
                    selectedOption = filters.value.category,
                    onOptionSelected = { filters.value = filters.value.copy(category = it) }
                )
                Spacer(modifier = Modifier.width(16.dp))
                DropdownMenuWithLabel(
                    label = "Language",
                    options = listOf("English", "Hindi", "Spanish"),
                    selectedOption = filters.value.language,
                    onOptionSelected = { filters.value = filters.value.copy(language = it) }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Show selected filters as chips
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (filters.value.category.isNotEmpty()) {
                    FilterChip(label = "Category: ${filters.value.category}") {
                        filters.value = filters.value.copy(category = "")
                    }
                }
                if (filters.value.language.isNotEmpty()) {
                    FilterChip(label = "Language: ${filters.value.language}") {
                        filters.value = filters.value.copy(language = "")
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChip(label: String, onClear: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.caption.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary
                )
            )
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(onClick = onClear) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_close_24),
                    contentDescription = "Remove Filter",
                    tint = MaterialTheme.colors.primary
                )
            }
        }
    }
}

@Composable
fun UnifiedTopBar(
    searchQuery: MutableState<String>,
    isSearchBarVisible: MutableState<Boolean>,
    filters: MutableState<FilterState>,
    onToggleSearch: () -> Unit
) {
    Column {
        EnhancedSearchBar(searchQuery = searchQuery, isSearchBarVisible = isSearchBarVisible, onToggleSearch = onToggleSearch)
        if (isSearchBarVisible.value) {
            EnhancedFilterPanel(filters = filters)
        }
    }
}

@Composable
fun DropdownMenuWithLabel(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        Text(label, style = MaterialTheme.typography.subtitle1, color = Color.Black)
        Box {
            TextButton(onClick = { expanded = true }) {
                Text(selectedOption.ifEmpty { "Select $label" }, color = Color.Black)
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_drop_down_24),
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { option ->
                    DropdownMenuItem(onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }) {
                        Text(option)
                    }
                }
            }
        }
    }
}




@Composable
fun LoadingView() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = ColorRepository.ForestGreen
        )
        Text(
            text = "Loading...",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
fun ErrorView(errorMessage: String?) {
    Text(
        text = "Error Occurred: $errorMessage",
        color = ColorRepository.Red,
        style = MaterialTheme.typography.body1,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun CategoryHeader(category: String) {
    Text(
        text = category,
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp),
        color = Color.Black,
        style = MaterialTheme.typography.h6.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryRow(items: List<Item>, navController: NavController) {
    Box(modifier = Modifier.height(220.dp)) {
        LazyRow {
            items(items, key = { it.id }) { item ->
                BrowserItem(
                    item = item,
                    drawable = item.image,
                    onItemClick = { navController.navigate("details/${item.id}") }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BrowserItem(item: Item, drawable: String, onItemClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .size(200.dp)
            .border(1.dp, ColorRepository.Gray, RoundedCornerShape(12.dp)),
        onClick = onItemClick,
        elevation = 8.dp,
        shape = RoundedCornerShape(12.dp),
        backgroundColor = Color.White
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.subtitle1.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                modifier = Modifier.padding(bottom = 8.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(ColorRepository.LightGray, ColorRepository.Gray)
                        )
                    )
            ) {
                val painter = rememberAsyncImagePainter(model = drawable)
                Image(
                    painter = painter,
                    contentDescription = item.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                if (painter.state is AsyncImagePainter.State.Loading) {
                    CircularProgressIndicator(
                        color = ColorRepository.ForestGreen,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                if (painter.state is AsyncImagePainter.State.Error) {
                    Icon(
                        painter = painterResource(id = R.drawable.error),
                        contentDescription = "Error",
                        tint = ColorRepository.Red,
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
}

