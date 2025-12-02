package com.example.presentation.screen.search

import android.location.Location
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.glance.text.Text
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.domain.model.feature.hospitals.HospitalDetail
import com.example.domain.model.feature.hospitals.HospitalMarker
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.atom.BasicButton
import com.example.presentation.component.ui.atom.BasicDialog
import com.example.presentation.component.ui.atom.ButtonType
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.organism.AppTopBar
import com.example.presentation.component.ui.organism.BottomNavigationBar
import com.example.presentation.component.ui.organism.CurrentBottomNav
import com.example.presentation.component.ui.organism.TopBarInfo
import com.example.presentation.utils.NaverMapView
import com.example.presentation.utils.error.ErrorDialog
import com.example.presentation.utils.error.ErrorDialogState
import com.example.presentation.utils.nav.ScreenDestinations
import com.example.presentation.utils.nav.safeNavigate
import com.example.presentation.utils.nav.safePopBackStack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun SearchScreen(
    navController: NavController,
    argument: SearchArgument,
    data: SearchData
) {
    var errorDialogState by remember { mutableStateOf(ErrorDialogState.idle()) }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    val currentUserLocation = data.currentUserLocation
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
                    text = "병원 검색",
                    isLeadingIconAvailable = false,
                    onLeadingIconClicked = {},
                    leadingIconResource = IconResource.Vector(Icons.AutoMirrored.Filled.KeyboardArrowLeft),
                    trailingIcons = listOf(
                        Pair(
                            IconResource.Vector(Icons.Default.Search),
                            {}
                        )
                    )
                ),
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = CurrentBottomNav.SEARCH,
                navController = navController
            )
        },
        containerColor = colorScheme.bgFrameDefault
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            SearchScreenContents(currentUserLocation, hospital)
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

    BasicDialog(
        onDismissRequest = { navController.safePopBackStack() }
    ){
        Text(
            text = "준비 중인 기능입니다.",
            style = MaterialTheme.typography.bodyLarge.emp(),
            color = colorScheme.commonText,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = "아직 개발중인 기능입니다.",
            style = MaterialTheme.typography.bodyMedium.emp(),
            color = colorScheme.caption,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        BasicButton(
            text = "닫기",
            onClicked = { navController.safePopBackStack() },
            type = ButtonType.PRIMARY,
        )
    }
    // BackHandler { }
}

@Composable
private fun SearchScreenContents(
    currentUserLocation: Location,
    hospital: HospitalDetail?
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        if (hospital != null) {
            NaverMapView(
                modifier = Modifier.height(400.dp),
                currentLocation = currentUserLocation,
                cameraPosition = Location("hospital").apply {
                    latitude = hospital.location.lat
                    longitude = hospital.location.lng
                },
                places = listOf(
                    HospitalMarker(
                        hospitalId = hospital.hospitalId,
                        latitude = hospital.location.lat,
                        longitude = hospital.location.lng,
                        isOpened = hospital.openNow,
                        isSelected = true
                    )
                ),
                selectedHospitalId = hospital.hospitalId,
                onMapReady = { },
                onMapBoundsChange = { },
                onMarkerClicked = { }
            )
        }
    }
}


@Composable
private fun SearchScreenPreview() {
    PetbulanceTheme() {
        SearchScreen(
            navController = rememberNavController(),
            argument = SearchArgument(
                intent = { },
                state = SearchState.Init,
                event = MutableSharedFlow()
            ),
            data = SearchData.empty()
        )
    }
}