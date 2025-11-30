package com.example.presentation.screen.report.contents

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.model.feature.diagnosis.FirstAidGuide
import com.example.domain.model.feature.diagnosis.FirstAidGuideContent
import com.example.domain.model.type.EmergencyLevel
import com.example.domain.model.type.toKorean
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.CommonPadding
import com.example.presentation.component.ui.atom.BasicButton
import com.example.presentation.component.ui.atom.BasicCard
import com.example.presentation.component.ui.atom.ButtonType
import com.example.presentation.component.ui.molecule.WarningCard
import com.example.presentation.screen.report.ReportArgument
import com.example.presentation.screen.report.ReportIntent
import com.example.presentation.screen.report.ReportScreenState
import com.example.presentation.screen.report.ReportState
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun FirstAidGuideContents(
    argument: ReportArgument,
    animalType: String,
    emergencyLevel: EmergencyLevel,
    suspectedDisease: String,
    firstAidGuide: FirstAidGuide,
    onHospitalMatchingResultClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(CommonPadding)
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SummaryCard(emergencyLevel, animalType, suspectedDisease)

            Text(
                text = "단계별 응급처치",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF143048),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(vertical = 8.dp),
                style = MaterialTheme.typography.labelLarge.emp(),
                textAlign = TextAlign.Center,
                color = Color.White
            )

            FirstAidGuideColumn(firstAidGuide = firstAidGuide)

            WarningCard()
        }
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BasicButton(
                text = "지금 진료 가능한 병원 찾기",
                type = ButtonType.PRIMARY,
                onClicked = onHospitalMatchingResultClicked
            )

            BasicButton(
                text = "AI 챗봇에게 질문하기",
                type = ButtonType.SECONDARY,
                onClicked = {/* TODO : navigate to AI Chatbot */ }
            )
        }
    }

    BackHandler {
        argument.reportIntent(ReportIntent.ScreenTransition(ReportScreenState.SummaryReport))
    }
}

@Composable
private fun SummaryCard(
    emergencyLevel: EmergencyLevel,
    animalType: String,
    suspectedDisease: String,
) {
    BasicCard {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "응급도",
                style = MaterialTheme.typography.labelSmall.emp(),
                color = colorScheme.caption
            )
            Text(
                text = emergencyLevel.toKorean(),
                style = MaterialTheme.typography.labelSmall,
                color = colorScheme.textPrimary
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "동물종",
                style = MaterialTheme.typography.labelSmall.emp(),
                color = colorScheme.caption
            )
            Text(
                text = animalType,
                style = MaterialTheme.typography.labelSmall,
                color = colorScheme.textPrimary
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "의심 증상",
                style = MaterialTheme.typography.labelSmall.emp(),
                color = colorScheme.caption
            )
            Text(
                text = suspectedDisease,
                style = MaterialTheme.typography.labelSmall,
                color = colorScheme.textPrimary
            )
        }
    }
}

@Composable
private fun FirstAidGuideColumn(firstAidGuide: FirstAidGuide) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        var idx = 1

        firstAidGuide.steps.forEach { elem ->
            BasicCard(
                cardPaddingValue = 16.dp
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    NumberedCircle(idx++)

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = elem.description,
                            style = MaterialTheme.typography.labelMedium.emp(),
                            color = colorScheme.textPrimary
                        )
                        Text(
                            text = elem.warning,
                            style = MaterialTheme.typography.labelSmall.emp(),
                            color = colorScheme.caption2
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NumberedCircle(number: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(24.dp)
            .background(
                color = colorScheme.primary,
                shape = CircleShape
            )
    ) {
        Text(
            text = "$number",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), // 2. 굵은 스타일 적용
            color = Color.White
        )
    }
}

@Preview(apiLevel = 34)
@Composable
private fun FirstAidGuidePreview() {
    PetbulanceTheme {
        FirstAidGuideContents(
            argument = ReportArgument(
                reportIntent = { },
                state = ReportState.Init,
                screenState = ReportScreenState.FirstAidGuide,
                event = MutableSharedFlow(),
                diagnosisIntent = { }
            ),
            animalType = "앵무새",
            emergencyLevel = EmergencyLevel.LOW,
            suspectedDisease = "위장관 울혈 증후군 의심",
            firstAidGuide = FirstAidGuide(
                steps = listOf(
                    FirstAidGuideContent(
                        description = "주변을 정리하고 안전한 공간으로 옮겨주세요",
                        warning = "날개가 떨어지지 않도록, 개별 케이지 안에 두세요"
                    ),
                    FirstAidGuideContent(
                        description = "주변을 정리하고 안전한 공간으로 옮겨주세요",
                        warning = "날개가 떨어지지 않도록, 개별 케이지 안에 두세요"
                    ),
                    FirstAidGuideContent(
                        description = "주변을 정리하고 안전한 공간으로 옮겨주세요",
                        warning = "날개가 떨어지지 않도록, 개별 케이지 안에 두세요"
                    ),
                ),
                totalSteps = 3
            ),
            {}
        )
    }
}
