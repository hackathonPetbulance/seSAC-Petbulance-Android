package com.example.presentation.component.ui.molecule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme

@Composable
fun WarningCard() {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFF5F5F5),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = "이 내용은 보호자의 판단을 돕기 위해 펫뷸런스 AI가 제공하는 응급 처치 가이드로, " +
                    "조금이라도 상태가 심각해 보이거나 애매하면 즉시 동물병원을 방문해 주세요.",
            style = MaterialTheme.typography.labelSmall,
            color = colorScheme.textPrimary
        )
    }
}
