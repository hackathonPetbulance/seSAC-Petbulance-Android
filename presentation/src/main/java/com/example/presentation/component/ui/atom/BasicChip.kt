package com.example.presentation.component.ui.atom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.theme.emp

@Composable
fun BasicChip(
    text: String,
    textColor: Color = colorScheme.textPrimary,
    backgroundColor: Color
) {
    Box(
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape((20.dp)))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.emp(),
            color = textColor
        )
    }
}