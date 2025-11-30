package com.example.presentation.screen.diagnosis.report.contents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.model.feature.hospitals.MatchedHospital
import com.example.domain.model.type.EmergencyLevel
import com.example.domain.model.type.HospitalFilterType
import com.example.domain.model.type.toKorean
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.ui.CommonPadding
import com.example.presentation.component.ui.atom.BasicButton
import com.example.presentation.component.ui.atom.BasicCard
import com.example.presentation.component.ui.atom.BasicIcon
import com.example.presentation.component.ui.atom.BasicSelectableChip
import com.example.presentation.component.ui.atom.ButtonType
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.molecule.HospitalCard

@Composable
fun HospitalMatchingResult(
    hospitals: List<MatchedHospital>,
    userLocation: String,
    emergencyLevel: EmergencyLevel,
    animalType: String,
    onFirstAidGuideClicked: () -> Unit
) {
    var currentFilter by remember { mutableStateOf(HospitalFilterType.DISTANCE) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(CommonPadding)
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            ResultCard(
                userLocation = userLocation,
                hospitalCounts = hospitals.size,
                emergencyLevel = emergencyLevel,
                animalType = animalType
            )

            FilterChip(
                currentFilter = currentFilter,
                onFilterChipClicked = { currentFilter = it }
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                hospitals.forEach { hospital ->
                    HospitalCard(hospital)
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0XFFF5F5F5),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                BasicIcon(
                    iconResource = IconResource.Vector(Icons.Default.CheckCircle),
                    contentDescription = "Verified",
                    size = 16.dp,
                    tint = Color(0XFF067DFD)
                )
                Text(
                    text = " 2025.11.28 팀 직접 전화 검증 완료",
                    style = MaterialTheme.typography.labelSmall,
                    color = colorScheme.textPrimary
                )
            }
        }
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BasicButton(
                text = "응급처치 가이드 보기",
                type = ButtonType.SECONDARY,
                onClicked = onFirstAidGuideClicked
            )
        }
    }
}

@Composable
private fun ResultCard(
    userLocation: String,
    hospitalCounts: Int,
    emergencyLevel: EmergencyLevel,
    animalType: String
) {
    BasicCard(
        contentPaddingValue = 8.dp,
        backgroundColor = colorScheme.primary.copy(alpha = 0.1f),
        borderColor = colorScheme.primary.copy(alpha = 0.2f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "$userLocation 기준 ${hospitalCounts}개 병원을 찾았어요.",
            color = colorScheme.textPrimary,
            style = MaterialTheme.typography.bodyLarge,
        )

        Text(
            text = "응급도 ${emergencyLevel.toKorean()} / $animalType / 현재 시간 기준 진료 가능",
            style = MaterialTheme.typography.labelSmall,
            color = colorScheme.caption
        )
    }
}

@Composable
private fun FilterChip(
    currentFilter: HospitalFilterType,
    onFilterChipClicked: (HospitalFilterType) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        HospitalFilterType.entries.forEach { elem ->
            BasicSelectableChip(
                text = elem.toKorean(),
                isSelected = (currentFilter.name == elem.name),
                onClicked = { onFilterChipClicked(elem) }
            )
        }
    }
}

@Preview(apiLevel = 34)
@Composable
private fun HospitalMatchingResultPreview() {
    PetbulanceTheme {
        HospitalMatchingResult(
            hospitals = listOf(MatchedHospital.stub(), MatchedHospital.stub()),
            userLocation = "서울 마포구",
            emergencyLevel = EmergencyLevel.MIDDLE,
            animalType = "앵무새",
            onFirstAidGuideClicked = {}
        )
    }
}