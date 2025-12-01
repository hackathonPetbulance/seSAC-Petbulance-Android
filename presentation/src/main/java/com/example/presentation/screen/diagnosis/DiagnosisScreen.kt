package com.example.presentation.screen.diagnosis

import android.Manifest
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.domain.model.type.HospitalFilterType
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.CommonPadding
import com.example.presentation.component.ui.atom.BasicButton
import com.example.presentation.component.ui.atom.BasicButtonWithIcon
import com.example.presentation.component.ui.atom.BasicCard
import com.example.presentation.component.ui.atom.BasicDialog
import com.example.presentation.component.ui.atom.BasicIcon
import com.example.presentation.component.ui.atom.BasicImageBox
import com.example.presentation.component.ui.atom.BasicInputTextField
import com.example.presentation.component.ui.atom.ButtonType
import com.example.presentation.component.ui.atom.CustomGreenLoader
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.organism.AppTopBar
import com.example.presentation.component.ui.organism.BottomNavigationBar
import com.example.presentation.component.ui.organism.CurrentBottomNav
import com.example.presentation.component.ui.organism.TopBarAlignment
import com.example.presentation.component.ui.organism.TopBarInfo
import com.example.presentation.utils.PermissionHandler
import com.example.presentation.utils.error.ErrorDialog
import com.example.presentation.utils.error.ErrorDialogState
import com.example.presentation.utils.hooks.rememberCameraLauncher
import com.example.presentation.utils.hooks.rememberPhotoPickerLauncher
import com.example.presentation.utils.nav.ScreenDestinations
import com.example.presentation.utils.nav.safeNavigate
import com.example.presentation.utils.nav.safePopBackStack
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow

@RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
@Composable
fun DiagnosisScreen(
    navController: NavController,
    argument: DiagnosisArgument,
    data: DiagnosisData
) {
    var errorDialogState by remember { mutableStateOf(ErrorDialogState.idle()) }
    val screenState = argument.screenState

    var launchCamera by remember { mutableStateOf(false) }
    var currentSelectedStep by remember { mutableIntStateOf(1) }

    var isHelpDialogVisible by remember { mutableStateOf(false) }
    var isAddImageDialogVisible by remember { mutableStateOf(false) }

    val cameraLauncher = rememberCameraLauncher { uri ->
        argument.intent(DiagnosisIntent.UpdateImageUris(uri, currentSelectedStep))
    }

    val galleryLauncher = rememberPhotoPickerLauncher(multiple = false) { uri ->
        if (uri.isNotEmpty()) {
            argument.intent(DiagnosisIntent.UpdateImageUris(uri.first(), currentSelectedStep))
        }
    }

    var isReportReady by remember { mutableStateOf(false) }
    var currentStep by remember { mutableIntStateOf(0) }

    val randomList = generateSequence {
        List(5) { (1..4).random() }
    }.first { it.sum() in 10..15 }

    val timer by remember { mutableStateOf(randomList) }

    LaunchedEffect(argument.screenState) {
        if (argument.screenState == DiagnosisScreenState.OnProgress) {
            currentStep = 0 // OnProgress 시작 시 0으로 초기화
            while (currentStep < 5) {
                delay(timer[currentStep] * 1000L) // 2.5초 대기
                currentStep++
            }
        }
    }

    LaunchedEffect(isReportReady, currentStep) {
        Log.d("siria22", "isReportReady = $isReportReady / currentStep = $currentStep")
        if (isReportReady && currentStep >= 5) {
            navController.safeNavigate(ScreenDestinations.Report.route)
            argument.intent(DiagnosisIntent.SwitchScreenState)
        }
    }


    LaunchedEffect(argument.event) {
        argument.event.collect { event ->
            when (event) {
                is DiagnosisEvent.Request.OnProgress -> {}

                is DiagnosisEvent.Request.Success -> {
                    isReportReady = true
                    argument.intent(DiagnosisIntent.MatchHospitalByFilter(HospitalFilterType.DISTANCE))
                }

                is DiagnosisEvent.Request.Error -> {
                    errorDialogState = ErrorDialogState.fromErrorEvent(event)
                }

                is DiagnosisEvent.DataFetch.Error -> {
                    errorDialogState = ErrorDialogState.fromErrorEvent(event)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            if (screenState == DiagnosisScreenState.Upload) {
                AppTopBar(
                    topBarInfo = TopBarInfo(
                        text = "펫뷸런스 AI",
                        textAlignment = TopBarAlignment.START,
                        isLeadingIconAvailable = true,
                        onLeadingIconClicked = { navController.safePopBackStack() },
                        leadingIconResource = IconResource.Vector(Icons.AutoMirrored.Filled.KeyboardArrowLeft),
                        trailingIcons = listOf(
                            Pair(
                                IconResource.Vector(Icons.Outlined.Info)
                            ) { isHelpDialogVisible = true }
                        )
                    ),
                )
            } else {
                AppTopBar(
                    topBarInfo = TopBarInfo(
                        isLoading = true,
                        text = "AI 증상 분석 중",
                        textAlignment = TopBarAlignment.START,
                        isLeadingIconAvailable = true,
                        onLeadingIconClicked = { argument.intent(DiagnosisIntent.StopDiagnosis) },
                        leadingIconResource = IconResource.Vector(Icons.AutoMirrored.Filled.KeyboardArrowLeft),
                    ),
                )
            }
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = CurrentBottomNav.AI,
                navController = navController
            )
        },
        containerColor = colorScheme.bgFrameDefault
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (argument.screenState == DiagnosisScreenState.Upload) {
                DiagnosisUploadScreenContents(
                    imageUris = data.imageUris,
                    animalSpecies = data.animalSpecies,
                    description = data.description,
                    onAnimalSpeciesNameChanged = {
                        argument.intent(DiagnosisIntent.UpdateAnimalSpecies(it))
                    },
                    onDescriptionChanged = { argument.intent(DiagnosisIntent.UpdateDescription(it)) },
                    onAddImageIconClicked = { idx ->
                        currentSelectedStep = idx
                        isAddImageDialogVisible = true
                    },
                    onRequestDiagnosisButtonClicked = {
                        argument.intent(
                            DiagnosisIntent.RequestDiagnosis(
                                onUpload = { _, _ -> }
                            )
                        )
                    },
                    onDeletionIconClicked = { idx ->
                        currentSelectedStep = idx
                        argument.intent(DiagnosisIntent.UpdateImageUris(null, currentSelectedStep))
                    }
                )
            } else {
                DiagnosisOnProgressScreenContents(
                    imageUris = data.imageUris,
                    currentStep = currentStep
                )
            }
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

    if (launchCamera) {
        PermissionHandler(
            permission = Manifest.permission.CAMERA,
            onPermissionGranted = {
                cameraLauncher()
                launchCamera = false
            },
            onPermissionDenied = {
                Log.d("siria22", "카메라 권한이 거부되었습니다.")
                launchCamera = false
            }
        )
    }
    if (isHelpDialogVisible) {
        BasicDialog(
            onDismissRequest = { isHelpDialogVisible = false }
        ) {
            Text(
                text = "왜 3장이 필요한가요?",
                color = Color.Black,
                style = typography.titleSmall.emp(),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "• 원격 진료 표준은 공통적으로 전신/부위/근접의 3단계 다각도 촬영을 권장합니다",
                style = typography.bodySmall,
                color = Color(0xFF5A5A5A)
            )
            Text(
                text = "• 특수동물은 아픈 티를 잘 숨깁니다. 한 장의 사진만으로는 놓치는 정보가 많습니다.",
                style = typography.bodySmall,
                color = Color(0xFF5A5A5A)
            )
            Text(
                text = "• 3장의 사진으로 전체 분석이 약 120초 이내에 끝나도록 설계했습니다.",
                style = typography.bodySmall,
                color = Color(0xFF5A5A5A)
            )
        }
    }

    if (isAddImageDialogVisible) {
        BasicDialog(
            onDismissRequest = { isAddImageDialogVisible = false }
        ) {
            Text(
                text = "증상이 보이는 사진을 선택하거나\n" +
                        "카메라로 직접 촬영하세요",
                textAlign = TextAlign.Center,
                style = typography.labelLarge.emp(),
                color = Color(0xFF5A5A5A),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            BasicButtonWithIcon(
                modifier = Modifier.fillMaxWidth(0.7f),
                text = "카메라로 촬영",
                onClicked = {
                    launchCamera = true
                    isAddImageDialogVisible = false
                },
                type = ButtonType.PRIMARY,
                iconResource = IconResource.Vector(Icons.Outlined.CameraAlt),
            )
            BasicButtonWithIcon(
                modifier = Modifier.fillMaxWidth(0.7f),
                text = "갤러리에서 선택",
                onClicked = {
                    galleryLauncher()
                    isAddImageDialogVisible = false
                },
                type = ButtonType.DEFAULT,
                iconResource = IconResource.Vector(Icons.Outlined.Upload)
            )
        }
    }
}

@Composable
private fun DiagnosisOnProgressScreenContents(
    imageUris: List<Uri?>,
    currentStep: Int
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .padding(CommonPadding),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val topImage = imageUris.firstOrNull { it != null }
        if (topImage != null) {
            BasicImageBox(
                size = 280.dp,
                galleryUri = topImage,
                isClearable = false
            )
        }

        HorizontalDivider(thickness = 1.dp, color = Color.LightGray)

        ProgressBar(currentStep = currentStep)

        AnalysisSteps(currentStep = currentStep)
    }
}

@Composable
private fun ProgressBar(currentStep: Int) {

    val targetProgress = (currentStep / 5f)
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        label = "Progress Animation"
    )
    val percentage = (animatedProgress * 100).toInt()

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = CommonPadding)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "분석 진행률",
                style = typography.bodyLarge.emp(),
                color = colorScheme.textPrimary
            )
            Text(
                text = "$percentage%",
                style = typography.bodyLarge,
                color = colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = colorScheme.primary,
            trackColor = Color(0xFFEEEEEE)
        )
        Text(
            text = "정확한 분석을 위해 신중하게 검토하고 있습니다",
            style = typography.bodySmall,
            color = colorScheme.textSecondary,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun AnalysisSteps(currentStep: Int) {
    val steps = listOf(
        "이미지 전처리" to "업로드된 사진의 품질을 최적화하고 증상 부위를 중심으로 분석하고 있습니다",
        "AI 비전 모델 로딩" to "특수동물 전문 AI 모델을 불러오고 전신 상태를 종합적으로 평가하고 있습니다",
        "증상 패턴 인식" to "다양한 각도에서 주요 증상을 탐지하고 응급도를 판단하고 있습니다",
        "의료 데이터베이스 매칭" to "팀 펫뷸런스가 수집한 종별 주요증상 DB와 비교 분석 중입니다",
        "응급도 분석 결과 생성" to "최종 분석 결과와 권장 조치사항을 정리하고 있습니다"
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "펫뷸런스 AI가 사진을 분석하고 있습니다.",
            style = typography.bodyMedium.emp(),
            color = colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        steps.forEachIndexed { index, (title, description) ->
            val stepNumber = index + 1
            val isCompleted = stepNumber < currentStep
            val isInProgress = stepNumber == currentStep

            BasicCard(
                modifier = Modifier.fillMaxWidth(),
                cardPaddingValue = 0.dp,
                backgroundColor = Color(0xFFF5F5F5),
                borderColor = Color.Transparent
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(modifier = Modifier.size(24.dp)) {
                        when {
                            isInProgress -> CustomGreenLoader(modifier = Modifier.align(Alignment.Center))
                            else -> BasicIcon(
                                iconResource = IconResource.Vector(Icons.Default.Check),
                                contentDescription = "Step $stepNumber",
                                size = 16.dp,
                                tint = if (isCompleted) colorScheme.primary else Color(0xFFEEEEEE)
                            )
                        }
                    }
                    Column {
                        Text(
                            text = "${stepNumber}단계 : $title",
                            style = typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.textPrimary
                        )
                        Text(
                            text = description,
                            style = typography.labelMedium,
                            color = colorScheme.textPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DiagnosisUploadScreenContents(
    imageUris: List<Uri?>,
    animalSpecies: String,
    description: String,
    onAnimalSpeciesNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onRequestDiagnosisButtonClicked: () -> Unit,
    onAddImageIconClicked: (Int) -> Unit,
    onDeletionIconClicked: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .padding(CommonPadding),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        UploadedState(imageUris)

        RequestMoreInfoCard()

        for (idx in 0..2) {
            UploadCard(
                imageUris[idx],
                index = idx,
                onAddImageIconClicked = { onAddImageIconClicked(idx) },
                onDeletionIconClicked = { onDeletionIconClicked(idx) }
            )
        }

        UserInputSection(
            animalSpecies = animalSpecies,
            description = description,
            onAnimalSpeciesNameChanged = onAnimalSpeciesNameChanged,
            onDescriptionChanged = onDescriptionChanged
        )

        /* TODO : 업로드 조건 확인 */
        if (false) {
            BasicButton(
                text = "AI 증상 분석하기 (${imageUris.filter { it != null }.size}/3)",
                type = ButtonType.DEFAULT,
                onClicked = { }
            )
        } else {
            BasicButton(
                text = "AI 증상 분석하기 (${imageUris.filter { it != null }.size}/3)",
                type = ButtonType.PRIMARY,
                onClicked = onRequestDiagnosisButtonClicked
            )
        }
    }
}

@Composable
private fun UploadedState(imageUris: List<Uri?>) {

    val uploadedImageCount = imageUris.filter { it != null }.size
    val progress = uploadedImageCount / 3f

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "사진을 올려주세요.",
            color = Color.Black,
            style = typography.titleSmall.emp(),
        )
        Text(
            text = "최소 1장, 최대 3장까지 선택 가능",
            style = typography.labelSmall,
            color = colorScheme.caption,
        )

        Column(
            modifier = Modifier.fillMaxWidth(0.8f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = colorScheme.primary,
                trackColor = Color.LightGray,
                strokeCap = StrokeCap.Round
            )
            Text(
                text = "$uploadedImageCount/3 장 업로드",
                style = typography.labelSmall,
                color = colorScheme.caption
            )
        }
    }
}

@Composable
private fun UploadCard(
    imageUri: Uri?,
    index: Int,
    onAddImageIconClicked: () -> Unit,
    onDeletionIconClicked: () -> Unit,
) {
    val recommendationText = if (index == 0) "필수" else "권장"
    val recommendationTextColor = if (index == 0) Color(0xFFEF4343) else Color(0xFFFFBB00)

    val recommendationDescriptionText = when (index) {
        0 -> "1단계: 아픈 부위를 중심으로 찍어주세요"
        1 -> "2단계: 전신을 찍어주세요"
        else -> "3단계: 다른 각도나 증상 파악에 도움이 되는 부분을 찍어주세요"
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        BasicCard(
            modifier = Modifier
                .size(150.dp)
                .drawBehind {
                    val stroke = Stroke(
                        width = 4.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )
                    drawRoundRect(
                        color = Color(0xFFEDEDED),
                        style = stroke,
                        cornerRadius = CornerRadius(16.dp.toPx())
                    )
                }
                .clickable {
                    onAddImageIconClicked()
                },
            borderColor = Color.Transparent,
            backgroundColor = Color(0xFFFAFAFA),
            cardPaddingValue = 0.dp
        ) {
            if (imageUri == null) {
                BasicIcon(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(48.dp),
                    iconResource = IconResource.Vector(Icons.Default.Add),
                    contentDescription = "Add image",
                    size = 1.dp,
                    tint = Color(0xFF9E9E9E)
                )
            } else {
                BasicImageBox(
                    modifier = Modifier.fillMaxWidth(),
                    galleryUri = imageUri,
                    size = 240.dp,
                    isClearable = true,
                    onDeletionIconClicked = onDeletionIconClicked
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = recommendationText,
                color = recommendationTextColor,
                style = typography.bodyLarge.emp()
            )
            Text(
                text = recommendationDescriptionText,
                color = colorScheme.caption,
                style = typography.bodyMedium
            )
        }
    }
}

@Composable
private fun RequestMoreInfoCard() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFF5F5F5), shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        BasicIcon(
            iconResource = IconResource.Vector(Icons.Outlined.Info),
            contentDescription = "Info Icon",
            size = 12.dp,
            tint = colorScheme.iconTint
        )
        Text(
            text = "증상을 직접 적으면 더 정확하게 분석합니다",
            style = typography.labelMedium,
            color = colorScheme.textPrimary,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserInputSection(
    animalSpecies: String,
    description: String,
    onAnimalSpeciesNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit
) {
    var isMenuOpened by remember { mutableStateOf(false) }
    val animalTypes = listOf("소형 포유류", "파충류", "조류")

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "텍스트로 직접 설명하기",
            color = colorScheme.textPrimary,
            style = typography.titleSmall.emp(),
        )

        Box {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.clickable { isMenuOpened = !isMenuOpened },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = animalSpecies.ifBlank { "동물종" },
                        color = colorScheme.textPrimary,
                        style = typography.bodyLarge.emp(),
                    )
                    BasicIcon(
                        iconResource = IconResource.Vector(Icons.Default.KeyboardArrowDown),
                        contentDescription = "Drop down menu",
                        size = 20.dp,
                        tint = colorScheme.textPrimary
                    )
                }

                Text(
                    text = "주요 증상",
                    color = colorScheme.textPrimary,
                    style = typography.bodyLarge.emp(),
                )

                BasicInputTextField(
                    value = description,
                    onValueChange = { onDescriptionChanged(it) },
                    placeholder = "예: 식욕 없음, 활동량 감소, 배변 이상 등",
                    singleLine = false,
                    sizeFactor = 4
                )
            }
            if (isMenuOpened) {
                Box(modifier = Modifier.padding(top = 24.dp)) {
                    DropdownMenu(
                        expanded = true,
                        onDismissRequest = { isMenuOpened = false },
                        modifier = Modifier.background(Color.White, RoundedCornerShape(4.dp)),
                    ) {
                        animalTypes.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = {
                                    val backgroundColor =
                                        if (selectionOption == animalSpecies) Color(0xFFEEEEEE)
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
                                    onAnimalSpeciesNameChanged(selectionOption)
                                    isMenuOpened = false
                                },
                                contentPadding = PaddingValues(vertical = 0.dp, horizontal = 4.dp),
                                modifier = Modifier.height(32.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(apiLevel = 34)
@Composable
private fun UserTextInputSectionPreview() {
    PetbulanceTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(color = Color.White)
                .padding(24.dp)
        ) {
            UserInputSection(
                animalSpecies = "조류",
                description = "",
                onAnimalSpeciesNameChanged = { },
                onDescriptionChanged = { }
            )
        }
    }
}

@RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
@Preview(apiLevel = 34)
@Composable
private fun DiagnosisScreenPreview() {
    PetbulanceTheme {
        DiagnosisScreen(
            navController = rememberNavController(),
            argument = DiagnosisArgument(
                intent = { },
                state = DiagnosisState.Init,
                screenState = DiagnosisScreenState.Upload,
                event = MutableSharedFlow()
            ),
            data = DiagnosisData.empty()
        )
    }
}