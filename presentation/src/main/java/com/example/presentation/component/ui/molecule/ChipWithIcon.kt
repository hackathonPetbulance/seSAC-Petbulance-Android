package com.example.presentation.component.ui.molecule

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.atom.BasicIcon
import com.example.presentation.component.ui.atom.IconResource

@Composable
fun ChipWithIcon(
    modifier : Modifier = Modifier,
    info: String,
    iconResource: IconResource,
    backgroundColor: Color,
    contentColor: Color,
    borderColor: Color
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape((20.dp)))
            .background(color = backgroundColor, RoundedCornerShape((20.dp)))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        BasicIcon(
            iconResource = iconResource,
            contentDescription = "$info Icon",
            size = 20.dp,
            tint = contentColor
        )
        Text(
            text = info,
            style = MaterialTheme.typography.labelMedium.emp(),
            color = contentColor
        )
    }
}