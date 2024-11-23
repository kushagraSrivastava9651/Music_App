package com.example.musicappui.ui.theme

 import androidx.compose.foundation.shape.RoundedCornerShape
 import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
 import androidx.compose.ui.text.font.FontFamily
 import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
 import androidx.compose.ui.unit.dp
 import androidx.compose.ui.unit.sp
val CustomTypography = Typography(
    defaultFontFamily = FontFamily.Serif,
    h1 = TextStyle(
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    ),
    // Define other text styles as needed...
)

val CustomShapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(16.dp),
    // Other shapes as needed...
)


val LightColorPalette = lightColors(
    primary = Color.Blue,
    secondary = Color.Green,
    background = Color.White, // Light mode background color
    // Other colors...
)

val DarkColorPalette = darkColors(
    primary = Color.Cyan,
    secondary = Color.Yellow,
    background = Color.LightGray, // Dark mode background color
    // Other colors...
)
@Composable
fun MyThemedApp(
    isDarkModeEnabled: Boolean,
    content: @Composable () -> Unit
) {
    val colors = if (isDarkModeEnabled) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = CustomTypography,
        shapes = CustomShapes,
        content = content
    )
}
