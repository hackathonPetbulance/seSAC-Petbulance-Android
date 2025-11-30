package com.example.presentation.component.ui.molecule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.domain.model.feature.hospitals.MatchedHospital
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.Dot
import com.example.presentation.component.ui.atom.BasicButton
import com.example.presentation.component.ui.atom.BasicCard
import com.example.presentation.component.ui.atom.BasicChip
import com.example.presentation.component.ui.atom.BasicImageBox
import com.example.presentation.component.ui.atom.ButtonType
import com.example.presentation.component.ui.dropShadow

@Composable
fun HospitalCard(hospital: MatchedHospital) {
    BasicCard(
        modifier = Modifier.dropShadow(
            shape = RoundedCornerShape(16.dp),
            color = Color.LightGray.copy(0.15f),
            blur = 2.dp,
            offsetY = 1.dp,
            spread = 1.dp
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicImageBox(
                size = 80.dp,
                galleryUri = hospital.thumbnailUrl.toUri(),
            )
            HospitalInfos(hospital)
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicButton(
                text = "길안내",
                type = ButtonType.SECONDARY,
                onClicked = { /* TODO : on Navigation Opened */ },
                modifier = Modifier.weight(1f)
            )
            BasicButton(
                text = "전화하기",
                type = ButtonType.PRIMARY,
                onClicked = { /* TODO : on Phone Call Intent */ },
                modifier = Modifier.weight(2f)
            )
        }
    }
}

@Composable
private fun HospitalInfos(hospital: MatchedHospital) {
    val commonRowSpacing = 8.dp
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(commonRowSpacing),
        ) {
            Text(
                text = hospital.name,
                color = colorScheme.textPrimary,
                style = MaterialTheme.typography.titleSmall.emp()
            )
            if (hospital.isOpenNow) {
                Text(
                    text = "진료 중",
                    style = MaterialTheme.typography.bodyMedium.emp(),
                    color = Color(0XFF067DFD)
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(commonRowSpacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "${hospital.distanceKm}km",
                style = MaterialTheme.typography.bodyMedium.emp(),
                color = colorScheme.caption
            )
            Dot(colorScheme.caption.copy(alpha = 0.8f))
            Text(
                text = "${hospital.todayCloseTime}에 영업 종료",
                style = MaterialTheme.typography.bodyMedium.emp(),
                color = colorScheme.textTertiary
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(commonRowSpacing)
        ) {
            hospital.treatableAnimals.forEach { elem ->
                BasicChip(elem, backgroundColor = Color(0XFFF6EF89))
            }
        }
    }
}

@Preview(apiLevel = 34)
@Composable
private fun HospitalCardPreview() {
    PetbulanceTheme {
        HospitalCard(MatchedHospital.stub())
    }
}