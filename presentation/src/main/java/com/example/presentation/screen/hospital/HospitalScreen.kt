package com.example.presentation.screen.hospital

import android.content.Intent
import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.domain.model.feature.hospitals.HospitalDetail
import com.example.domain.model.feature.hospitals.OpenHours
import com.example.domain.model.feature.review.Review
import com.example.domain.model.feature.review.ReviewList
import com.example.domain.utils.toKorean
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.CommonPadding
import com.example.presentation.component.ui.atom.BasicButton
import com.example.presentation.component.ui.atom.BasicIcon
import com.example.presentation.component.ui.atom.ButtonType
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.molecule.ChipWithIcon
import com.example.presentation.component.ui.molecule.HospitalInfoCard
import com.example.presentation.component.ui.organism.AppTopBar
import com.example.presentation.component.ui.organism.BottomNavigationBar
import com.example.presentation.component.ui.organism.CurrentBottomNav
import com.example.presentation.component.ui.organism.TopBarAlignment
import com.example.presentation.component.ui.organism.TopBarInfo
import com.example.presentation.utils.NaverMapView
import com.example.presentation.utils.error.ErrorDialog
import com.example.presentation.utils.error.ErrorDialogState
import com.example.presentation.utils.nav.ScreenDestinations
import com.example.presentation.utils.nav.safeNavigate
import com.example.presentation.utils.nav.safePopBackStack
import com.naver.maps.map.NaverMap
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun HospitalScreen(
    navController: NavController,
    argument: HospitalArgument,
    data: HospitalData
) {
    var errorDialogState by remember { mutableStateOf(ErrorDialogState.idle()) }

    val hospital = data.hospital
    val currentUserLocation = data.currentUserLocation
    val reviews = data.reviews

    LaunchedEffect(argument.event) {
        argument.event.collect { event ->
            when (event) {

                is HospitalEvent.DataFetch.Error -> {
                    errorDialogState = ErrorDialogState.fromErrorEvent(event)
                }

                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = "병원 상세 정보",
                    textAlignment = TopBarAlignment.CENTER,
                    isLeadingIconAvailable = true,
                    onLeadingIconClicked = { navController.safePopBackStack() },
                    leadingIconResource = IconResource.Vector(Icons.AutoMirrored.Filled.KeyboardArrowLeft),
                    trailingIcons = listOf(
                        Pair(
                            IconResource.Vector(Icons.Filled.Share),
                            { }
                        )
                    )
                ),
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = CurrentBottomNav.AI,
                navController = navController
            )
        },
        containerColor = colorScheme.bgFrameDefault,
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            HospitalScreenContents(
                hospital = hospital,
                currentUserLocation = currentUserLocation,
                reviews = reviews ?: ReviewList.empty(),
                onNavigateButtonClicked = {
                    navController.safeNavigate(
                        ScreenDestinations.Search.createRoute(
                            hospital.hospitalId
                        )
                    )
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

    // BackHandler { }
}

private enum class TabType {
    DETAILS, REVIEWS
}

@Composable
private fun HospitalScreenContents(
    hospital: HospitalDetail,
    currentUserLocation: Location,
    reviews: ReviewList,
    onNavigateButtonClicked: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(TabType.DETAILS) }
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            HospitalInfoCard(hospital = hospital, modifier = Modifier.padding(CommonPadding))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = CommonPadding)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedTab = TabType.DETAILS }
                        .drawBehind {
                            if (selectedTab == TabType.DETAILS) {
                                val strokeWidth = 2.dp.toPx()
                                drawLine(
                                    color = Color.Black,
                                    start = Offset(0f, size.height - strokeWidth / 2),
                                    end = Offset(size.width, size.height - strokeWidth / 2),
                                    strokeWidth = strokeWidth
                                )
                            }
                        }
                ) {
                    Text(
                        text = "상세정보",
                        color = colorScheme.textPrimary,
                        style = typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(12.dp)
                    )
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedTab = TabType.REVIEWS }
                        .drawBehind {
                            if (selectedTab == TabType.REVIEWS) {
                                val strokeWidth = 2.dp.toPx()
                                drawLine(
                                    color = Color.Black,
                                    start = Offset(0f, size.height - strokeWidth / 2),
                                    end = Offset(size.width, size.height - strokeWidth / 2),
                                    strokeWidth = strokeWidth
                                )
                            }
                        }
                ) {
                    Text(
                        text = "방문 후기",
                        color = colorScheme.textPrimary,
                        style = typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
            HorizontalDivider(thickness = (0.5).dp, color = Color.LightGray)

            if (selectedTab == TabType.DETAILS) {
                DetailTab(hospital, currentUserLocation, onNavigateButtonClicked)
            } else {
                ReviewTab(reviews)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(CommonPadding),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BasicButton(
                text = "전화 문의하기",
                type = ButtonType.PRIMARY,
                onClicked = {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = "tel:${hospital.phone}".toUri()
                    }
                    context.startActivity(intent)
                }
            )
        }
    }
}

@Composable
private fun DetailTab(
    hospital: HospitalDetail,
    currentLocation: Location,
    onNavigateButtonClicked: () -> Unit
) {
    var naverMap by remember { mutableStateOf<NaverMap?>(null) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(CommonPadding)
    ) {
        Text(
            text = "병원 위치",
            color = colorScheme.textPrimary,
            style = typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
        )
        Text(
            text = "${hospital.location.address}\n${hospital.name}",
            color = colorScheme.textTertiary,
            style = typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
        )
        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        ) {
            NaverMapView(
                currentLocation = currentLocation,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(8.dp)),
                places = listOf(hospital.toMarker()),
                onMapReady = { map -> naverMap = map },
                onMapBoundsChange = {},
                selectedHospitalId = hospital.hospitalId,
                onMarkerClicked = { },
                cameraPosition = Location("").apply {
                    latitude = hospital.location.lat
                    longitude = hospital.location.lng
                }
            )
        }
        BasicButton(
            text = "지도에서 보기",
            type = ButtonType.DEFAULT,
            onClicked = onNavigateButtonClicked,
        )
    }

    HorizontalDivider(thickness = 12.dp, color = Color(0XFFEEEEEE))

    OpenInfo(hospital.openHours)

    HorizontalDivider(thickness = 12.dp, color = Color(0XFFEEEEEE))

    ProposeModificationsCard()

    HorizontalDivider(thickness = 1.dp, color = Color.LightGray)

    Spacer(modifier = Modifier.height(64.dp))
}

@Composable
private fun OpenInfo(data: List<OpenHours>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(CommonPadding)
    ) {
        Text(
            text = "영업 정보",
            color = colorScheme.textPrimary,
            style = typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            data.chunked(2).forEach { rowItems ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rowItems.forEach { item ->
                        HourInfoItem(
                            day = item.day.toKorean(),
                            hours = item.hours,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowItems.size == 1) {
                        Box(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun HourInfoItem(day: String, hours: String, modifier: Modifier = Modifier) {
    val dayColor = when (day) {
        "토요일" -> Color.Blue
        "일요일", "공휴일" -> Color.Red
        else -> colorScheme.textSecondary
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        Text(
            text = day,
            color = dayColor,
            style = typography.bodyMedium.emp(),
            modifier = Modifier.weight(0.3f),
            textAlign = TextAlign.Start
        )
        Text(
            text = hours,
            color = colorScheme.textSecondary,
            style = typography.bodyMedium.emp(),
            modifier = Modifier.weight(0.6f),
            textAlign = TextAlign.Start
        )
    }
}

@Composable
private fun ProposeModificationsCard() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(CommonPadding)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            BasicIcon(
                iconResource = IconResource.Vector(Icons.Default.Info),
                contentDescription = "Propose Modification",
                size = 28.dp,
                tint = colorScheme.iconTint
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "알고 계신 정보와 다른가요?",
                    color = colorScheme.textPrimary,
                    style = typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                )
                Text(
                    text = "잘못된 정보를 알려주시면 빠르게 반영할게요",
                    color = colorScheme.caption,
                    style = typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                )
            }

        }
        BasicIcon(
            iconResource = IconResource.Vector(Icons.AutoMirrored.Filled.KeyboardArrowRight),
            contentDescription = "navigate to page",
            size = 28.dp,
            tint = colorScheme.iconTint
        )
    }
}

@Composable
private fun ReviewTab(reviews: ReviewList) {

    var isMenuOpened by remember { mutableStateOf(false) }
    var currentSelectedSortOption by remember { mutableStateOf("최신순") }
    val sortOptions = listOf("최신순", "추천순", "별점 높은 순")

    var isContentsWithImagesOnly by remember { mutableStateOf(false) }
    var isVerifiedContentsOnly by remember { mutableStateOf(false) }

    val disabledChipColor = Color(0xFFB9C0C7)
    val activatedChipColor = colorScheme.primary

    val filteredReviewList =
        reviews.list.filter { if (isVerifiedContentsOnly) it.receiptCheck else true }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(CommonPadding)
            .fillMaxWidth()
    ) {
        Box {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = currentSelectedSortOption,
                    color = colorScheme.textPrimary,
                    style = typography.bodyLarge,
                    modifier = Modifier.clickable { isMenuOpened = !isMenuOpened },
                )
                BasicIcon(
                    iconResource = IconResource.Vector(Icons.Default.KeyboardArrowDown),
                    contentDescription = "Drop down menu",
                    size = 20.dp,
                    tint = colorScheme.textPrimary,
                    modifier = Modifier.clickable { isMenuOpened = !isMenuOpened },
                )
                ChipWithIcon(
                    modifier = Modifier.clickable {
                        isContentsWithImagesOnly = !isContentsWithImagesOnly
                    },
                    text = "사진 후기",
                    iconResource = IconResource.Vector(Icons.Default.CameraAlt),
                    backgroundColor = Color.Transparent,
                    contentColor = if (isContentsWithImagesOnly) activatedChipColor else disabledChipColor,
                    borderColor = if (isContentsWithImagesOnly) activatedChipColor else disabledChipColor
                )

                ChipWithIcon(
                    modifier = Modifier.clickable {
                        isVerifiedContentsOnly = !isVerifiedContentsOnly
                    },
                    text = "영수증 인증",
                    iconResource = IconResource.Vector(Icons.Default.Check),
                    backgroundColor = Color.Transparent,
                    contentColor = if (isVerifiedContentsOnly) activatedChipColor else disabledChipColor,
                    borderColor = if (isVerifiedContentsOnly) activatedChipColor else disabledChipColor
                )
            }

            if (isMenuOpened) {
                Box(modifier = Modifier.padding(top = 24.dp)) {
                    DropdownMenu(
                        expanded = true,
                        onDismissRequest = { isMenuOpened = false },
                        modifier = Modifier.background(
                            Color.White,
                            RoundedCornerShape(4.dp)
                        ),
                    ) {
                        sortOptions.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = {
                                    val backgroundColor =
                                        if (selectionOption == currentSelectedSortOption) Color(
                                            0xFFEEEEEE
                                        )
                                        else Color.Transparent
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                color = backgroundColor,
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                            .padding(4.dp)
                                    ) {
                                        Text(
                                            selectionOption,
                                            style = typography.bodyMedium,
                                            color = colorScheme.textPrimary
                                        )
                                    }
                                },
                                onClick = {
                                    currentSelectedSortOption = selectionOption
                                    isMenuOpened = false
                                },
                                contentPadding = PaddingValues(
                                    vertical = 0.dp,
                                    horizontal = 4.dp
                                ),
                                modifier = Modifier.height(32.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (filteredReviewList.isEmpty()) {
            Text(
                text = "표시할 내용이 없어요",
                color = colorScheme.textPrimary,
                style = typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.padding(12.dp)
            )
        } else {
            filteredReviewList.forEach { review ->
                HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
                ReviewCard(review = review)
            }
        }
    }
}

@Composable
private fun ReviewCard(review: Review) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(CommonPadding)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val animalCategory = review.detailAnimalType.split(">").firstOrNull()?.trim() ?: ""
                ChipWithIcon(
                    text = animalCategory,
                    iconResource = IconResource.Vector(Icons.Default.Check), // 아이콘은 적절히 변경 필요
                    backgroundColor = Color(0xFFFFF9E6),
                    contentColor = colorScheme.textSecondary,
                    borderColor = Color.Transparent
                )
                Text(text = "•", color = colorScheme.textSecondary, style = typography.bodySmall)
                Text(
                    text = review.author,
                    color = colorScheme.textSecondary,
                    style = typography.bodySmall
                )
                Text(text = "•", color = colorScheme.textSecondary, style = typography.bodySmall)
                Text(
                    text = "2025.11.10",
                    color = colorScheme.textSecondary,
                    style = typography.bodySmall
                )
            }
            BasicIcon(
                iconResource = IconResource.Vector(Icons.Default.MoreVert),
                contentDescription = "More",
                tint = colorScheme.textSecondary
            )
        }

        StarRating(rating = review.totalRating)

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = review.detailAnimalType,
                color = colorScheme.textPrimary,
                style = typography.bodyMedium
            )
            Text(
                text = review.treatmentService,
                color = colorScheme.textPrimary,
                style = typography.bodyMedium
            )
        }

        Text(
            text = review.reviewContent,
            color = colorScheme.textSecondary,
            style = typography.bodyMedium
        )

        // Footer
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (review.receiptCheck) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = colorScheme.textTertiary,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "영수증 인증 완료",
                        color = colorScheme.textTertiary,
                        style = typography.labelSmall
                    )
                    BasicIcon(
                        iconResource = IconResource.Vector(Icons.Default.CheckCircle),
                        contentDescription = "verified",
                        size = 12.dp,
                        tint = colorScheme.textTertiary
                    )
                }
            } else {
                Spacer(modifier = Modifier.size(4.dp))
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "도움이 됐어요",
                    color = colorScheme.caption,
                    style = typography.labelMedium
                )
                BasicIcon(
                    iconResource = IconResource.Vector(Icons.Default.ThumbUp),
                    contentDescription = "Helpful",
                    size = 16.dp,
                    tint = colorScheme.caption
                )
                Text(
                    text = "24",
                    color = colorScheme.caption,
                    style = typography.labelMedium
                )
            }
        }
    }
}

@Composable
private fun StarRating(rating: Double, maxRating: Int = 5) {
    Row {
        for (i in 1..maxRating) {
            val starIcon = when {
                rating >= i -> Icons.Filled.Star
                rating > i - 1 -> Icons.Filled.StarHalf
                else -> Icons.Filled.StarOutline
            }
            BasicIcon(
                iconResource = IconResource.Vector(starIcon),
                contentDescription = "Star",
                size = 20.dp,
                tint = Color(0xFFFFBF0F)
            )
        }
    }
}

@Preview(apiLevel = 34)
@Composable
private fun HospitalScreenPreview() {
    PetbulanceTheme {
        HospitalScreen(
            navController = rememberNavController(),
            argument = HospitalArgument(
                intent = { },
                state = HospitalState.Init,
                event = MutableSharedFlow()
            ),
            data = HospitalData.stub()
        )
    }
}
