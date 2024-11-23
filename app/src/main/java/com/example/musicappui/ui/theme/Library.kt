package com.example.musicappui.ui.theme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.musicappui.Lib
import com.example.musicappui.libraries

@Composable
fun Library(navController: NavController) {
    LazyColumn {
        items(libraries) { lib ->
            LibItem(lib = lib, onClick = {
                if (lib.name == "Favourite NewsList") {
                    navController.navigate("favorite_list_screen")
                }
                if (lib.name == "Famous News Articles") {
                    navController.navigate("famous_news_articles")
                }
                if (lib.name == "Contact Us") {
                    navController.navigate("contact_us")
                }
                if (lib.name == "About Us") {
                    navController.navigate("about_us")
                }

            })
        }
    }
}

@Composable
fun LibItem(lib: Lib, onClick: () -> Unit) {
    val backgroundColor = MaterialTheme.colors.background
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = 16.dp)
                .background(backgroundColor),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Icon(
                    painter = painterResource(id = lib.icon),
                    modifier = Modifier.padding(horizontal = 8.dp),
                    contentDescription = lib.name
                )
                Text(text = lib.name)
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Arrow Right"
            )
        }
        Divider(color = Color.DarkGray)    }
}
