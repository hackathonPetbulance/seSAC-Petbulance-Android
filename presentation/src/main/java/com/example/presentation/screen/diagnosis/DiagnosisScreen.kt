package com.example.presentation.screen.diagnosis

import android.Manifest
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.organism.AppTopBar
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
import kotlinx.coroutines.flow.MutableSharedFlow

@RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
@Composable
fun DiagnosisScreen(
    navController: NavController,
    argument: DiagnosisArgument,
    data: DiagnosisData
) {
    var errorDialogState by remember { mutableStateOf(ErrorDialogState.idle()) }

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
        },
        containerColor = colorScheme.bgFrameDefault
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            DiagnosisScreenContents(
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
                    /* TODO : Navigate to next page : 로딩 */
                },
                onDeletionIconClicked = { idx ->
                    currentSelectedStep = idx
                    argument.intent(DiagnosisIntent.UpdateImageUris(null, currentSelectedStep))
                }
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
private fun DiagnosisScreenContents(
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
                strokeCap = StrokeCap.Butt
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
        horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                },
            borderColor = Color.Transparent,
            backgroundColor = Color(0xFFFAFAFA),
            cardPaddingValue = 0.dp
        ) {
            if (imageUri == null) {
                BasicIcon(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            onAddImageIconClicked()
                        },
                    iconResource = IconResource.Vector(Icons.Default.Add),
                    contentDescription = "Add image",
                    size = 12.dp,
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
                style = typography.labelMedium.emp()
            )
            Text(
                text = recommendationDescriptionText,
                color = colorScheme.caption,
                style = typography.labelMedium.emp()
            )
        }
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
            text = "직접 입력하기",
            color = colorScheme.textPrimary,
            style = typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
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
                        style = typography.bodyLarge,
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
                    style = typography.bodyLarge,
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

@Preview(apiLevel = 34)
@Composable
private fun UploadCardPreview() {
    PetbulanceTheme {
        UploadCard(null, 1, {}, {})
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
                event = MutableSharedFlow()
            ),
            data = DiagnosisData.empty()
        )
    }
}