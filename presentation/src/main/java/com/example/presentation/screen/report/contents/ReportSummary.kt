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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.model.type.EmergencyLevel
import com.example.domain.model.type.toKorean
import com.example.presentation.R
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.CommonPadding
import com.example.presentation.component.ui.atom.BasicButton
import com.example.presentation.component.ui.atom.BasicCard
import com.example.presentation.component.ui.atom.BasicChip
import com.example.presentation.component.ui.atom.BasicIcon
import com.example.presentation.component.ui.atom.ButtonType
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.molecule.WarningCard
import com.example.presentation.screen.report.ReportArgument
import com.example.presentation.screen.report.ReportScreenState
import com.example.presentation.screen.report.ReportState
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun ReportSummary(
    argument: ReportArgument,
    emergencyLevel: EmergencyLevel,
    detectedSymptom: List<String>,
    suspectedDisease: String,
    recommendedAction: List<String>,
    onHospitalMatchingResultClicked: () -> Unit,
    onFirstAidGuideClicked: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(CommonPadding)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .align(Alignment.TopCenter),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            EmergencyCard(emergencyLevel)

            AiSummaryCard(detectedSymptom, suspectedDisease, recommendedAction)

            WarningCard()
        }

        val hospitalButtonType = if(emergencyLevel == EmergencyLevel.HIGH) ButtonType.PRIMARY else ButtonType.SECONDARY
        val firstAidGuideButtonType = if(emergencyLevel == EmergencyLevel.HIGH) ButtonType.SECONDARY else ButtonType.PRIMARY

        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BasicButton(
                text = "지금 진료 가능한 병원",
                type = hospitalButtonType,
                onClicked = onHospitalMatchingResultClicked,
                modifier = Modifier.fillMaxWidth()
            )

            BasicButton(
                text = "응급처치 가이드 보기",
                type = firstAidGuideButtonType,
                onClicked = onFirstAidGuideClicked,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
    BackHandler {
        /* TODO : Back handler at Report Summary screen */
    }
}

@Composable
private fun EmergencyCard(emergencyLevel: EmergencyLevel) {

    val backgroundColor = when (emergencyLevel) {
        EmergencyLevel.HIGH -> Color(0xFFFFE3E3)
        EmergencyLevel.MIDDLE -> Color(0xFFFFF2CC)
        EmergencyLevel.LOW -> Color(0xFFDDEEFF)
    }

    val iconColor = when (emergencyLevel) {
        EmergencyLevel.HIGH -> Color(0xFFEF4343)
        EmergencyLevel.MIDDLE -> Color(0xFFFFBB00)
        EmergencyLevel.LOW -> Color(0xFF2667FF)
    }

    val descriptionText = when (emergencyLevel) {
        EmergencyLevel.HIGH -> "이 앱의 안내는 수의사의 직접 진료를 대체할 수 없습니다. 지금 바로 병원에 연락해 주세요."
        EmergencyLevel.MIDDLE -> "오늘 중 병원 진료를 권장합니다. 아래 안내를 참고해 현재 상태를 악화시키지 않도록 관리해 주세요."
        EmergencyLevel.LOW -> "즉시 응급으로 보이진 않지만, 증상이 지속되면 일상 상담 또는 병원 진료를 고려해 주세요."
    }


    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = iconColor, shape = RoundedCornerShape(1000.dp)
                )
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            BasicIcon(
                iconResource = IconResource.Vector(Icons.Outlined.WarningAmber),
                contentDescription = "Warning Marker",
                size = 24.dp,
                tint = Color.White
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "응급도 : ${emergencyLevel.toKorean()}",
                color = colorScheme.textPrimary,
                style = MaterialTheme.typography.titleSmall.emp(),
            )
            Text(
                text = descriptionText,
                style = MaterialTheme.typography.labelSmall.emp(),
                color = colorScheme.textPrimary
            )
        }
    }
}

@Composable
private fun AiSummaryCard(
    detectedSymptom: List<String>,
    suspectedDisease: String,
    recommendedAction: List<String>
) {
    BasicCard {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicIcon(
                iconResource = IconResource.Drawable(R.drawable.icon_pulse),
                contentDescription = "Pulse Icon",
                size = 18.dp,
                tint = colorScheme.primary
            )
            Text(
                text = "AI 분석 결과",
                color = colorScheme.textPrimary,
                style = MaterialTheme.typography.titleSmall.emp(),
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        Text(
            text = "감지된 주요 증상",
            style = MaterialTheme.typography.labelLarge.emp(),
            color = colorScheme.textPrimary
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            detectedSymptom.forEach { elem ->
                BasicChip(
                    text = elem,
                    textColor = colorScheme.primary,
                    backgroundColor = colorScheme.primary.copy(alpha = 0.1f)
                )
            }
        }

        HorizontalDivider(thickness = (0.5).dp, color = Color.LightGray)

        Text(
            text = "의심 증상",
            style = MaterialTheme.typography.labelLarge.emp(),
            color = colorScheme.textPrimary
        )

        Text(
            text = suspectedDisease,
            style = MaterialTheme.typography.labelMedium,
            color = colorScheme.textSecondary
        )

        HorizontalDivider(thickness = (0.5).dp, color = Color.LightGray)

        Text(
            text = "권장 조치",
            style = MaterialTheme.typography.labelLarge.emp(),
            color = colorScheme.textPrimary
        )

        recommendedAction.forEach { elem ->
            val text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(color = colorScheme.primary, fontWeight = FontWeight.W700)
                ) {
                    append("• ")
                }
                withStyle(
                    style = SpanStyle(
                        color = colorScheme.textSecondary,
                        fontWeight = FontWeight.W500
                    )
                ) {
                    append(elem)
                }
            }
            Text(
                text,
                style = MaterialTheme.typography.labelMedium,
                color = colorScheme.caption2
            )
        }
    }

}

@Preview(apiLevel = 34)
@Composable
private fun ReportSummaryPreview() {
    PetbulanceTheme {
        ReportSummary(
            emergencyLevel = EmergencyLevel.LOW,
            detectedSymptom = listOf("식욕 저하", "활동성 감소", "이거 난가?"),
            suspectedDisease = "위장관 울혈 증후군 의심",
            recommendedAction = listOf(
                "2시간 이내 전문의 진료 권장",
                "이동 중 보온 유지 필수",
                "강제 급식 금지"
            ),
            argument = ReportArgument(
                reportIntent = { },
                state = ReportState.Init,
                screenState = ReportScreenState.FirstAidGuide,
                event = MutableSharedFlow(),
                diagnosisIntent = { }
            ),
            onHospitalMatchingResultClicked = {},
            onFirstAidGuideClicked = {},
        )
    }
}