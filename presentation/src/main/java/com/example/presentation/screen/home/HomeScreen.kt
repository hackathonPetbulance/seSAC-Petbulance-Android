package com.example.presentation.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.organism.AppTopBar
import com.example.presentation.component.ui.organism.BottomNavigationBar
import com.example.presentation.component.ui.organism.CurrentBottomNav
import com.example.presentation.component.ui.organism.TopBarInfo
import com.example.presentation.utils.error.ErrorDialog
import com.example.presentation.utils.error.ErrorDialogState
import com.example.presentation.utils.nav.ScreenDestinations
import com.example.presentation.utils.nav.safeNavigate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun HomeScreen(
    navController: NavController,
    argument: HomeArgument,
    data: HomeData
) {
    var errorDialogState by remember { mutableStateOf(ErrorDialogState.idle()) }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

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
                    text = "Home",
                    isLeadingIconAvailable = false,
                    onLeadingIconClicked = {},
                    leadingIconResource = IconResource.Vector(Icons.AutoMirrored.Filled.KeyboardArrowLeft),
                    isTrailingIconAvailable = false,
                    onTrailingIconClicked = {},
                    trailingIconResource = IconResource.Vector(Icons.Filled.MoreVert)
                ),
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = CurrentBottomNav.HOME,
                navController = navController
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            HomeScreenContents(

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

    // BackHandler {  }
}

@Composable
private fun HomeScreenContents(

) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text("text")

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
            data = HomeData(
                data = ""
            )
        )
    }
}