package com.example.presentation.screen.hospital

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.domain.model.feature.hospitals.HospitalDetail
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.ui.CommonPadding
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.molecule.HospitalInfoCard
import com.example.presentation.component.ui.organism.AppTopBar
import com.example.presentation.component.ui.organism.TopBarAlignment
import com.example.presentation.component.ui.organism.TopBarInfo
import com.example.presentation.utils.error.ErrorDialog
import com.example.presentation.utils.error.ErrorDialogState
import com.example.presentation.utils.nav.ScreenDestinations
import com.example.presentation.utils.nav.safeNavigate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun HospitalScreen(
    navController: NavController,
    argument: HospitalArgument,
    data: HospitalData
) {
    var errorDialogState by remember { mutableStateOf(ErrorDialogState.idle()) }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    val hospital = data.hospital

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
                    text = "병원 상세 정보",
                    textAlignment = TopBarAlignment.CENTER,
                    isLeadingIconAvailable = true,
                    onLeadingIconClicked = {},
                    leadingIconResource = IconResource.Vector(Icons.AutoMirrored.Filled.KeyboardArrowLeft),
                    trailingIcons = listOf(
                        Pair(
                            IconResource.Vector(Icons.Filled.Share),
                            { /* TODO : Share icon */ }
                        )
                    )
                ),
            )
        },
        containerColor = PetbulanceTheme.colorScheme.bgFrameDefault,
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            HospitalScreenContents(
                hospital
            )
        }
    }

    if (errorDialogState.isErrorDialogVisible) {
        ErrorDialog(
            errorDialogState = errorDialogState,
            errorHandler = {
                errorDialogState = errorDialogState.toggleVisibility()
                navController.safeNavigate(ScreenDestinations.Home.route)
            }
        )
    }

    // BackHandler { }
}

@Composable
private fun HospitalScreenContents(
    hospital: HospitalDetail
) {
    var selectedTab by remember { mutableStateOf(TabType.DETAILS) }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(CommonPadding)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        HospitalInfoCard(hospital = hospital, modifier = Modifier.fillMaxWidth())

        HospitalDetailsTab(selectedTab)
    }
}

@Composable
private fun HospitalDetailsTab(selectedTab: TabType){

}

private enum class TabType {
    DETAILS, REVIEWS
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
            data = HospitalData(
                hospital = HospitalDetail.stub
            )
        )
    }
}