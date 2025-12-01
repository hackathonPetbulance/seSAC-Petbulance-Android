package com.example.presentation.screen.report

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.domain.model.feature.diagnosis.AiDiagnosis
import com.example.domain.model.feature.diagnosis.FirstAidGuide
import com.example.domain.model.feature.diagnosis.FirstAidGuideContent
import com.example.domain.model.feature.hospitals.MatchedHospital
import com.example.domain.model.type.EmergencyLevel
import com.example.domain.model.type.HospitalFilterType
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.organism.AppTopBar
import com.example.presentation.component.ui.organism.BottomNavigationBar
import com.example.presentation.component.ui.organism.CurrentBottomNav
import com.example.presentation.component.ui.organism.TopBarAlignment
import com.example.presentation.component.ui.organism.TopBarInfo
import com.example.presentation.screen.diagnosis.DiagnosisIntent
import com.example.presentation.screen.report.contents.FirstAidGuideContents
import com.example.presentation.screen.report.contents.HospitalMatchingResult
import com.example.presentation.screen.report.contents.ReportSummary
import com.example.presentation.utils.error.ErrorDialog
import com.example.presentation.utils.error.ErrorDialogState
import com.example.presentation.utils.nav.ScreenDestinations
import com.example.presentation.utils.nav.safeNavigate
import com.example.presentation.utils.nav.safePopBackStack
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun ReportScreen(
    navController: NavController,
    argument: ReportArgument,
    data: ReportData
) {
    var errorDialogState by remember { mutableStateOf(ErrorDialogState.idle()) }

    val screenState = argument.screenState

    val aiDiagnosis = data.aiDiagnosis
    val userLocation = data.userLocation
    val matchedHospitals = data.matchedHospitals

    LaunchedEffect(argument.event) {
        argument.event.collect { event ->
            when (event) {
                is ReportEvent.DataFetch.Error -> {
                    errorDialogState = ErrorDialogState.fromErrorEvent(event)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = "AI 분석 결과",
                    textAlignment = TopBarAlignment.START,
                    isLeadingIconAvailable = true,
                    onLeadingIconClicked = { navController.safePopBackStack() },
                    leadingIconResource = IconResource.Vector(Icons.AutoMirrored.Filled.KeyboardArrowLeft),
                ),
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = CurrentBottomNav.AI,
                navController = navController
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            ReportScreenContents(
                argument = argument,
                screenState = screenState,
                aiDiagnosis = aiDiagnosis,
                userLocation = userLocation,
                matchedHospitals = matchedHospitals,
                onHospitalMatchingResultClicked = {
                    argument.reportIntent(ReportIntent.ScreenTransition(ReportScreenState.HospitalMatching))
                },
                onFirstAidGuideClicked = {
                    argument.reportIntent(ReportIntent.ScreenTransition(ReportScreenState.FirstAidGuide))
                },
                onHospitalMatchRequest = { filter ->
                    argument.diagnosisIntent(DiagnosisIntent.MatchHospitalByFilter(filter))
                },
                onHospitalCardClicked = { id ->
                    navController.safeNavigate(ScreenDestinations.Hospital.createRoute(id))
                },
                onNavigateButtonClicked = { id ->
                    navController.safeNavigate(ScreenDestinations.Search.createRoute(id))
                }
            )
        }
    }

    if (errorDialogState.isErrorDialogVisible) {
        ErrorDialog(
            errorDialogState = errorDialogState,
            errorHandler = {
                errorDialogState = errorDialogState.toggleVisibility()
            }
        )
    }

    BackHandler { }
}

@Composable
private fun ReportScreenContents(
    argument: ReportArgument,
    screenState: ReportScreenState,
    aiDiagnosis: AiDiagnosis,
    userLocation: String,
    matchedHospitals: List<MatchedHospital>,
    onHospitalMatchingResultClicked: () -> Unit,
    onFirstAidGuideClicked: () -> Unit,
    onHospitalMatchRequest: (HospitalFilterType) -> Unit,
    onHospitalCardClicked: (Long)-> Unit,
    onNavigateButtonClicked: (Long) ->Unit
) {
    when (screenState) {
        ReportScreenState.SummaryReport -> ReportSummary(
            argument = argument,
            emergencyLevel = aiDiagnosis.emergencyLevel,
            detectedSymptom = aiDiagnosis.detectedSymptoms,
            suspectedDisease = aiDiagnosis.suspectedDisease,
            recommendedAction = aiDiagnosis.recommendedActions,
            onHospitalMatchingResultClicked = onHospitalMatchingResultClicked,
            onFirstAidGuideClicked = onFirstAidGuideClicked
        )

        ReportScreenState.HospitalMatching -> HospitalMatchingResult(
            argument = argument,
            hospitals = matchedHospitals,
            userLocation = userLocation,
            emergencyLevel = aiDiagnosis.emergencyLevel,
            animalType = aiDiagnosis.animalType,
            onFirstAidGuideClicked = onFirstAidGuideClicked,
            onHospitalMatchRequest = onHospitalMatchRequest,
            onHospitalCardClicked = onHospitalCardClicked,
            onNavigateButtonClicked = onNavigateButtonClicked
        )

        ReportScreenState.FirstAidGuide -> FirstAidGuideContents(
            argument = argument,
            animalType = aiDiagnosis.animalType,
            emergencyLevel = aiDiagnosis.emergencyLevel,
            suspectedDisease = aiDiagnosis.suspectedDisease,
            firstAidGuide = aiDiagnosis.firstAidGuide,
            onHospitalMatchingResultClicked = onHospitalMatchingResultClicked
        )
    }
}


@Preview(apiLevel = 34)
@Composable
private fun ReportScreenPreview() {
    PetbulanceTheme {
        ReportScreen(
            navController = rememberNavController(),
            argument = ReportArgument(
                reportIntent = { },
                state = ReportState.Init,
                screenState = ReportScreenState.FirstAidGuide,
                event = MutableSharedFlow(),
                diagnosisIntent = { }
            ),
            data = ReportData(
                aiDiagnosis = AiDiagnosis(
                    animalType = "앵무새",
                    emergencyLevel = EmergencyLevel.MIDDLE,
                    detectedSymptoms = listOf("식욕 저하", "활동성 감소", "이거 난가?"),
                    suspectedDisease = "위장관 울혈 증후군 의심",
                    recommendedActions = listOf(
                        "2시간 이내 전문의 진료 권장",
                        "이동 중 보온 유지 필수",
                        "강제 급식 금지"
                    ),
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
                    confidence = 0.88f
                ),
                userLocation = "서울 마포시",
                matchedHospitals = listOf(MatchedHospital.stub(), MatchedHospital.stub())
            )
        )
    }
}
