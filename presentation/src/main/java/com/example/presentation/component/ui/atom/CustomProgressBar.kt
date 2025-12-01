package com.example.presentation.component.ui.atom

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme

@Composable
fun CustomGreenLoader(
    modifier: Modifier = Modifier
) {
    val progressColor = colorScheme.primary // 진한 초록 (Indicator)
    val trackColor = Color(0xFFE0F2F1)    // 연한 민트/초록 (Background)

    CircularProgressIndicator(
        modifier = modifier.size(24.dp),
        color = progressColor,                   // 돌아가는 선 색상
        trackColor = trackColor,                 // 배경 원 색상
        strokeWidth = 4.dp,                      // 선 두께
        strokeCap = StrokeCap.Round              // 선 끝을 둥글게 (이미지와 동일)
    )
}

@Composable
fun ContentPlaceholder() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CustomGreenLoader()
    }
}

@Preview(apiLevel = 34)
@Composable
private fun CustomGreenLoaderPreview() {
    PetbulanceTheme {
        CustomGreenLoader()
    }
}