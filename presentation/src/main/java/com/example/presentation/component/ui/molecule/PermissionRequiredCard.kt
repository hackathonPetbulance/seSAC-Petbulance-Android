package com.example.presentation.component.ui.molecule

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.atom.BasicCard

@Composable
fun PermissionRequiredCard(
    missingPermissions: String,
) {
    BasicCard(
        backgroundColor = Color(0xFFFDFDFD),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "이 기능을 이용하려면 아래의 권한이 필요해요:",
            style = typography.bodyMedium.emp(),
            color = colorScheme.textPrimary
        )
        Text(
            text = "• $missingPermissions",
            style = typography.bodySmall,
            color = colorScheme.textSecondary,
        )
    }
}

@Preview
@Composable
private fun PermissionRequiredCardPreview() {
    PetbulanceTheme {
        PermissionRequiredCard(missingPermissions = "위치 권한")
    }
}