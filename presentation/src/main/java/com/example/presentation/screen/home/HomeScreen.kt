package com.example.presentation.screen.home

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.domain.model.feature.hospitals.MatchedHospital
import com.example.domain.model.feature.reviews.HospitalReview
import com.example.domain.model.type.AnimalCategory
import com.example.domain.model.type.toKorean
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.CommonPadding
import com.example.presentation.component.ui.Dot
import com.example.presentation.component.ui.atom.BasicButton
import com.example.presentation.component.ui.atom.BasicCard
import com.example.presentation.component.ui.atom.BasicIcon
import com.example.presentation.component.ui.atom.BasicSelectableChip
import com.example.presentation.component.ui.atom.ButtonType
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.molecule.HospitalCard
import com.example.presentation.component.ui.molecule.PermissionRequiredCard
import com.example.presentation.component.ui.organism.AppTopBar
import com.example.presentation.component.ui.organism.TopBarAlignment
import com.example.presentation.component.ui.organism.TopBarInfo
import com.example.presentation.utils.PermissionHandler
import com.example.presentation.utils.error.ErrorDialog
import com.example.presentation.utils.error.ErrorDialogState
import com.example.presentation.utils.nav.ScreenDestinations
import com.example.presentation.utils.nav.safeNavigate
import kotlinx.coroutines.flow.MutableSharedFlow
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(
    navController: NavController,
    argument: HomeArgument,
    data: HomeData
) {
    val context = LocalContext.current
    var errorDialogState by remember { mutableStateOf(ErrorDialogState.idle()) }
    var hasLocationPermission by remember { mutableStateOf(true) }

    var currentSelectedAnimalCategory by remember { mutableStateOf(AnimalCategory.SMALL_MAMMAL) }
    val onReviewCategoryChipClicked = { animalCategory: AnimalCategory ->
        currentSelectedAnimalCategory = animalCategory
    }

    val hospitalCards = data.matchedHospitals
    val hospitalReviews = data.hospitalReviews

    if (!hasLocationPermission) {
        PermissionHandler(
            permission = android.Manifest.permission.ACCESS_FINE_LOCATION,
            onPermissionGranted = { hasLocationPermission = true },
            onPermissionDenied = {
                Toast.makeText(context, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        )
    }

    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) argument.intent(HomeIntent.GetNearByHospital)
    }

    LaunchedEffect(argument.event) {
        argument.event.collect { event ->
            when (event) {
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = "펫뷸런스",
                    textAlignment = TopBarAlignment.START,
                    shouldEmphasized = true,
                    isLeadingIconAvailable = false,
                    isTrailingIconAvailable = true,
                    trailingIcons = listOf(Pair(IconResource.Vector(Icons.Filled.NotificationsNone)) {
                        /* TODO : do sth */
                    })
                ),
            )
        },
        containerColor = colorScheme.bgFrameDefault
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            HomeScreenContents(
                hasLocationPermission = hasLocationPermission,
                matchedHospitals = hospitalCards,
                hospitalReviews = hospitalReviews,
                currentSelectedAnimalCategory = currentSelectedAnimalCategory,
                onReviewCategoryChipClicked = onReviewCategoryChipClicked,
                onNavigateToDiagnosisScreen = {
                    navController.safeNavigate(ScreenDestinations.Diagnosis.route)
                },
            )
        }
    }

    if (errorDialogState.isErrorDialogVisible) {
        ErrorDialog(
            errorDialogState = errorDialogState,
            errorHandler = {
                errorDialogState.toggleVisibility()
                navController.safeNavigate(ScreenDestinations.Home.route)
            }
        )
    }

    BackHandler { }
}

@Composable
private fun HomeScreenContents(
    hasLocationPermission: Boolean,
    matchedHospitals: List<MatchedHospital>,
    hospitalReviews: List<HospitalReview>,
    currentSelectedAnimalCategory: AnimalCategory,
    onNavigateToDiagnosisScreen: () -> Unit,
    onReviewCategoryChipClicked: (AnimalCategory) -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .padding(CommonPadding),
        verticalArrangement = Arrangement.spacedBy(CommonPadding)
    ) {
        StartAiReportCard(onNavigateToDiagnosisScreen)

        NearByHospitalCards(hasLocationPermission, matchedHospitals)

        HospitalReviews(
            hospitalReviews = hospitalReviews,
            currentSelectedAnimalCategory = currentSelectedAnimalCategory,
            onReviewCategoryChipClicked = onReviewCategoryChipClicked,
        )
    }
}

@Composable
private fun StartAiReportCard(
    onButtonClicked: () -> Unit
) {
    val gradientColors = listOf(
        Color(0xFF1C334B).copy(alpha = 0.15f),
        Color(0xFFEF2A2A).copy(alpha = 0.30f)
    )

    val brush = Brush.linearGradient(
        colors = gradientColors,
        start = Offset(0f, 0f), // 왼쪽 (X=0, Y=0)
        end = Offset(1000f, 0f) // 오른쪽 (X는 임의의 큰 값, Y=0)
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(brush, RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = Color(0xFFEF4343),
                        shape = RoundedCornerShape(1000.dp)
                    )
                    .padding(4.dp)
            ) {
                Text(
                    "SOS",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W700),
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                val description = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(color = Color(0xFF0E2647), fontWeight = FontWeight.W400)
                    ) {
                        append("아이의 현재 상태를 촬영하면\n")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = colorScheme.warningText,
                            fontWeight = FontWeight.W600
                        )
                    ) {
                        append("펫뷸런스 AI")
                    }
                    withStyle(
                        style = SpanStyle(color = Color(0xFF0E2647), fontWeight = FontWeight.W400)
                    ) {
                        append("가 응급도를 판별하여 진료중인 병원을 안내하고 응급처치 가이드를 제공합니다.")
                    }
                }

                Text(
                    "응급 상황인가요?",
                    style = typography.titleSmall.emp(),
                    color = colorScheme.textSecondary
                )
                Text(
                    description,
                    style = typography.bodyMedium
                )
            }
        }
        BasicButton(
            text = "AI 응급 분석 시작하기",
            type = ButtonType.EMERGENCY,
            onClicked = onButtonClicked,
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(8.dp))
        )
    }
}

@Composable
private fun NearByHospitalCards(
    hasLocationPermission: Boolean,
    matchedHospitals: List<MatchedHospital>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "내 주변 진료중인 병원",
            color = colorScheme.textPrimary,
            style = typography.titleSmall.emp(),
            modifier = Modifier.padding(vertical = 4.dp)
        )
        if (hasLocationPermission) {
            matchedHospitals.forEach { hospital ->
                HospitalCard(hospital)
            }
        } else {
            PermissionRequiredCard("위치 정보 수집 권한")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HospitalReviews(
    hospitalReviews: List<HospitalReview>,
    currentSelectedAnimalCategory: AnimalCategory,
    onReviewCategoryChipClicked: (AnimalCategory) -> Unit
) {
    val listState = rememberLazyListState()
    val snapBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "병원 진료 후기",
            color = colorScheme.textPrimary,
            style = typography.titleSmall.emp(),
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            AnimalCategory.entries.forEach { elem ->
                BasicSelectableChip(
                    text = elem.toKorean(),
                    isSelected = (currentSelectedAnimalCategory.name == elem.name),
                    onClicked = { onReviewCategoryChipClicked(elem) }
                )
            }
        }

        LazyRow(
            state = listState,
            flingBehavior = snapBehavior,
            contentPadding = PaddingValues(horizontal = 0.dp),
        ) {
            items(hospitalReviews) { elem ->
                HospitalReviewCarouselCard(
                    modifier = Modifier.fillParentMaxWidth(),
                    review = elem
                )
            }
        }
    }
}
// 병원 후기에서는 페이징 없음 -> 필터링 된 결과 그냥 그대로 던져줌

@Composable
private fun HospitalReviewCarouselCard(
    modifier: Modifier = Modifier,
    review: HospitalReview
) {
    BasicCard(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = review.title,
                style = typography.run { titleSmall.emp() },
                color = colorScheme.reviewTextColor
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicIcon(
                    iconResource = IconResource.Vector(Icons.Outlined.CalendarToday),
                    contentDescription = "calendarToday",
                    size = 16.dp,
                    tint = colorScheme.caption2,
                )
                Text(
                    text = review.createdAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")),
                    style = typography.bodySmall.emp(),
                    color = colorScheme.caption2
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = review.hospitalName,
                style = typography.bodySmall.emp(),
                color = colorScheme.caption2
            )
            Dot(colorScheme.caption2, 1.dp)
            Text(
                text = review.animalCategory.toKorean(),
                style = typography.bodySmall.emp(),
                color = colorScheme.caption2
            )
        }

        Text(
            text = review.content.take(150),
            style = typography.bodySmall.emp(),
            color = colorScheme.reviewTextColor,
            softWrap = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    PetbulanceTheme {
        HomeScreen(
            navController = rememberNavController(),
            argument = HomeArgument(
                intent = { },
                state = HomeState.Init,
                event = MutableSharedFlow()
            ),
            data = HomeData.stub()
        )
    }
}