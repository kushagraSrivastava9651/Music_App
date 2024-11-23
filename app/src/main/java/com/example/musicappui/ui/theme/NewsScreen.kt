/*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.musicappui.R // Replace R with your actual package name

@Composable
fun TitleDescriptionScreen(itemId: Int, title: String, description: String, drawableResId: String) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )

        // Image below title
        val painter = rememberAsyncImagePainter(
            model = drawableResId
        )

        Image(
            painter = painter,
            contentDescription = null, // Provide appropriate content description
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 16.dp) // Add padding below the image
        )

        Text(
            text = description,
            style = MaterialTheme.typography.body1,
            color = Color.Gray,
            textAlign = TextAlign.Justify
        )
    }
}


 */
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
 import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
 import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.musicappui.R
 import com.example.musicappui.ui.theme.Item

// Define mutable list to store favorites
val favoriteList = mutableStateListOf<Item>()

// Define mutable list to store favorites

@Composable
fun TitleDescriptionScreen(itemId: Int, title: String, description: String, drawableResId: String) {
    val backgroundColor = Color(0xFFFAFAFA) // White background
    val primaryColor = Color(0xFF4A90E2) // Muted Blue
    val secondaryColor = Color(0xFF34C759) // Muted Green
    val textColor = Color.Black
    val context = LocalContext.current
    var isFavorite by remember { mutableStateOf(Item(description,itemId,drawableResId,title) in favoriteList) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(backgroundColor),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                // Title text
                Text(
                    text = title,
                    style = MaterialTheme.typography.h4.copy(color = primaryColor),
                    modifier = Modifier.padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )

                // Image below title
                val painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context)
                        .data(data = drawableResId)
                        .apply {
                            placeholder(R.drawable.loading) // Placeholder image
                            error(R.drawable.error) // Error image
                        }
                        .build()
                )

                Box(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .shadow(4.dp, shape = MaterialTheme.shapes.medium)
                        .background(Color.White, shape = MaterialTheme.shapes.medium)
                ) {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .size(240.dp)
                            .clip(MaterialTheme.shapes.medium)
                    )
                }

                // Description text
                Text(
                    text = description,
                    style = MaterialTheme.typography.body1,
                    color = textColor,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.widthIn(max = 400.dp)
                )
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Brand logo
                Image(
                    painter = painterResource(id = R.drawable.quick_news_logo),
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Favorite button
                    IconButton(
                        onClick = {
                            isFavorite = !isFavorite
                            if (isFavorite) {
                                favoriteList.add(Item(description,itemId,drawableResId,title))
                            } else {
                                favoriteList.remove(Item(description,itemId,drawableResId,title))
                            }
                        },
                        modifier = Modifier
                            .background(
                                if (isFavorite) secondaryColor.copy(alpha = 0.2f)
                                else primaryColor.copy(alpha = 0.1f),
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (isFavorite) R.drawable.baseline_favorite_24
                                else R.drawable.baseline_favorite_border_24
                            ),
                            contentDescription = "Favorite",
                            tint = if (isFavorite) secondaryColor else primaryColor
                        )
                    }

                    // Share button
                    IconButton(
                        onClick = {
                            shareContent(context, title, description)
                        },
                        modifier = Modifier
                            .background(primaryColor.copy(alpha = 0.1f), shape = MaterialTheme.shapes.small)
                            .padding(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_share_24),
                            contentDescription = "Share",
                            tint = primaryColor
                        )
                    }
                }
            }
        }
    }
}

fun shareContent(context: Context, title: String, description: String) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "$title\n\n$description")
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share via"))
}

@Preview(showBackground = true)
@Composable
fun PreviewTitleDescriptionScreen() {
    TitleDescriptionScreen(
        itemId = 1,
        title = "Sample Title",
        description = "This is a sample description for the item. It provides detailed information about the item, including its features, benefits, and usage instructions.",
        drawableResId = "https://via.placeholder.com/150"
    )
}
