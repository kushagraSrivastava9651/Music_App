package com.example.musicappui.ui.theme

import TitleDescriptionScreen
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.musicappui.R
import com.google.gson.Gson
@Composable
fun FavoriteListScreen(favoriteList: List<Item>,navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEAF4FA)) // Muted blue background
    ) {
        items(favoriteList) { item ->
            FavoriteItem(item = item,navController)
        }
    }
}

@Composable
fun FavoriteItem(item: Item, navController:NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp) // Spacing for the list item
            .clickable {
                navController.navigate(
                    "details_screen/${Uri.encode(Gson().toJson(item))}"

                )
            }
            .background(Color.White, shape = MaterialTheme.shapes.medium) // Card-like background
            .padding(8.dp)
    ) {
        // Avatar or Image Section
        AvatarImage(imageUrl = item.image)

        Spacer(modifier = Modifier.width(16.dp))

        // Text Content Section
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.subtitle1,
                color = Color.Black // Bold black title
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.description.take(30) + "...", // Truncated description
                style = MaterialTheme.typography.body2,
                color = Color.Gray // Muted gray for the description
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Time Placeholder
        Text(
            text = "1 min ago", // Example time placeholder
            style = MaterialTheme.typography.caption,
            color = Color(0xFF757575) // Light gray for timestamps
        )


    }
    Divider(color = Color.DarkGray)
}

@Composable
fun AvatarImage(imageUrl: String) {
    Box(
        modifier = Modifier
            .size(48.dp) // Fixed avatar size
            .background(
                color = Color(0xFFB0BEC5), // Muted blue-gray placeholder background
                shape = CircleShape
            )
    ) {
        if (imageUrl.isNotEmpty()) {
            // Load image if available
            Image(
                painter = rememberImagePainter(data = imageUrl),
                contentDescription = "Avatar",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        } else {
            // Default icon for placeholder
            Icon(
                painter = painterResource(id = R.drawable.baseline_article_24), // Replace with your icon
                contentDescription = "Placeholder Icon",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(24.dp),
                tint = Color.White
            )
        }
    }
}
