package com.example.presentation.component.theme

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private val primary = Color(0xFF2DA969)

private val LightColorPalette = PetbulanceColorScheme(
    isDark = false,

    lightPrimary = Color(0xFF8B87EA),
    secondary = Color(0xFF637387),
    commonText = Color(0xFF000000),
    descriptionText = Color(0xFF637387),
    iconTint = Color(0xFF424242),
    inactivatedIconColor = Color(0xFFBCBCBC),
    inactivatedTextColor = Color(0xFF555555),
    bottomNavIconTint = Color(0xFF637387),
    surface = Color(0xFFFEFEFE),
    background = Color(0xFFF3F4F6),

    primaryButtonColor = primary,
    onPrimaryButtonColor = Color(0xFFFFFFFF),
    secondaryButtonColor = Color(0xFFFFFFFF),
    onSecondaryButtonColor = primary,

    primary = Color(0xFF2DA969),
    textPrimary = Color(0xFF1E1E1E),
    textTertiary = Color(0xFF424242),
    reviewTextColor = Color(0xFF143048),

    bgFrameDefault = Color(0xFFFFFFFF),
    defaultIcon = Color(0XFF424242),

    warningText = Color(0xFFEF4343),
    textSecondary = Color(0xFF212121),

    caption = Color(0xFF9E9E9E),
    caption2 = Color(0xFF65758B),
    disabled = Color(0XFFBDBDBD)
)

private val DarkColorPalette = LightColorPalette.copy(isDark = true)

data class PetbulanceColorScheme(
    val isDark: Boolean,

    var lightPrimary: Color,
    var secondary: Color,
    var commonText: Color,
    var descriptionText: Color,
    var iconTint: Color,
    var inactivatedIconColor: Color,
    var inactivatedTextColor: Color,
    var bottomNavIconTint: Color,
    var surface: Color,
    var background: Color,

    var primaryButtonColor: Color,
    var onPrimaryButtonColor: Color,
    var secondaryButtonColor: Color,
    var onSecondaryButtonColor: Color,


    var primary: Color,
    var textPrimary: Color,
    var textSecondary: Color,
    var textTertiary: Color,
    var reviewTextColor: Color,

    var caption: Color,
    var caption2: Color,
    var disabled: Color,

    var defaultIcon: Color,
    var bgFrameDefault: Color,
    var warningText: Color,
)

val LocalPetbulanceColorScheme = staticCompositionLocalOf { LightColorPalette }

@Composable
fun getPetbulanceColorScheme(darkTheme: Boolean = false): PetbulanceColorScheme {
    return if (darkTheme) DarkColorPalette else LightColorPalette
}

@Preview(name = "Light Color Palette", showBackground = true, widthDp = 300)
@Preview(
    name = "Dark Color Palette",
    showBackground = true,
    widthDp = 300,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ColorPalettePreview() {
    PetbulanceTheme {
        val colors = listOf(
            "primary" to PetbulanceTheme.colorScheme.primary,
            "lightPrimary" to PetbulanceTheme.colorScheme.lightPrimary,
            "secondary" to PetbulanceTheme.colorScheme.secondary,
            "commonText" to PetbulanceTheme.colorScheme.commonText,
            "descriptionText" to PetbulanceTheme.colorScheme.descriptionText,
            "iconTint" to PetbulanceTheme.colorScheme.iconTint,
            "inactivatedColor" to PetbulanceTheme.colorScheme.inactivatedIconColor,
            "inactivatedTextColor" to PetbulanceTheme.colorScheme.inactivatedTextColor,
            "bottomNavIconTint" to PetbulanceTheme.colorScheme.bottomNavIconTint,
            "surface" to PetbulanceTheme.colorScheme.surface,
            "background" to PetbulanceTheme.colorScheme.background,
            "primaryButtonColor" to PetbulanceTheme.colorScheme.primaryButtonColor,
            "onPrimaryButtonColor" to PetbulanceTheme.colorScheme.onPrimaryButtonColor,
            "secondaryButtonColor" to PetbulanceTheme.colorScheme.secondaryButtonColor,
            "onSecondaryButtonColor" to PetbulanceTheme.colorScheme.onSecondaryButtonColor,
        )

        val backgroundColor =
            if (PetbulanceTheme.colorScheme.isDark) Color.Black else Color.White
        LazyColumn(
            modifier = Modifier
                .background(backgroundColor)
                .padding(16.dp)
        ) {
            items(colors) { (name, color) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .border(width = 1.dp, color = Color.Black)
                            .background(color)
                    )
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = PetbulanceTheme.colorScheme.commonText,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }
}