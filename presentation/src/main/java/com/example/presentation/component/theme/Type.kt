package com.example.presentation.component.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.R

// Set of Material typography styles to start with
val PretendardVariable = FontFamily(
    Font(R.font.pretendard_variable)
)

// Set of Material typography styles to start with
val PretendardTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = PretendardVariable,
        fontWeight = W400,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = PretendardVariable,
        fontWeight = W400,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = PretendardVariable,
        fontWeight = W400,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = PretendardVariable,
        fontWeight = W400,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = PretendardVariable,
        fontWeight = W400,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = PretendardVariable,
        fontWeight = W400,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = PretendardVariable,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = PretendardVariable,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),
    titleSmall = TextStyle(
        fontFamily = PretendardVariable,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = PretendardVariable,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = (0.5).sp
    ),
    bodyMedium = TextStyle(
        fontFamily = PretendardVariable,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = (0.25).sp
    ),
    bodySmall = TextStyle(
        fontFamily = PretendardVariable,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 16.sp,
        letterSpacing = (0.4).sp
    ),
    labelLarge = TextStyle(
        fontFamily = PretendardVariable,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 20.sp,
        letterSpacing = (0.1).sp
    ),
    labelMedium = TextStyle(
        fontFamily = PretendardVariable,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = (0.5).sp
    ),
    labelSmall = TextStyle(
        fontFamily = PretendardVariable,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = (0.5).sp
    ),
)

fun TextStyle.emp(): TextStyle {
    return this.copy(
        fontWeight = W600
    )
}

@Preview(apiLevel = 34)
@Composable
private fun TypographyPreview() {
    PetbulanceTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Sample Type headlineLarge",
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = "Sample Type headlineMedium",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Sample Type headlineSmall",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "Sample Type titleLarge",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Sample Type titleMedium",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Sample Type titleSmall",
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = "Sample Type bodyLarge",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Sample Type bodyMedium",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Sample Type bodySmall",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Sample Type labelLarge",
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = "Sample Type labelMedium",
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = "Sample Type labelSmall",
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = "Sample Type headlineLarge.emp()",
                style = MaterialTheme.typography.headlineLarge.emp()
            )
            Text(
                text = "Sample Type headlineMedium.emp()",
                style = MaterialTheme.typography.headlineMedium.emp()
            )
            Text(
                text = "Sample Type headlineSmall.emp()",
                style = MaterialTheme.typography.headlineSmall.emp()
            )
            Text(
                text = "Sample Type titleLarge.emp()",
                style = MaterialTheme.typography.titleLarge.emp()
            )
            Text(
                text = "Sample Type titleMedium.emp()",
                style = MaterialTheme.typography.titleMedium.emp()
            )
            Text(
                text = "Sample Type titleSmall.emp()",
                style = MaterialTheme.typography.titleSmall.emp()
            )
            Text(
                text = "Sample Type bodyLarge.emp()",
                style = MaterialTheme.typography.bodyLarge.emp()
            )
            Text(
                text = "Sample Type bodyMedium.emp()",
                style = MaterialTheme.typography.bodyMedium.emp()
            )
            Text(
                text = "Sample Type bodySmall.emp()",
                style = MaterialTheme.typography.bodySmall.emp()
            )
            Text(
                text = "Sample Type labelLarge.emp()",
                style = MaterialTheme.typography.labelLarge.emp()
            )
            Text(
                text = "Sample Type labelMedium.emp()",
                style = MaterialTheme.typography.labelMedium.emp()
            )
            Text(
                text = "Sample Type labelSmall.emp()",
                style = MaterialTheme.typography.labelSmall.emp()
            )
        }
    }
}