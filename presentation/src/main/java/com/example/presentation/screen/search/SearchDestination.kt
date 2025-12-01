package com.example.presentation.screen.search

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.searchDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.Search.route,
        arguments = listOf(
            navArgument(name = "hospitalId") {
                type = NavType.LongType
                defaultValue = 0L
            }
        )
    ) {
        val viewModel: SearchViewModel = hiltViewModel()

        val argument: SearchArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            SearchArgument(
                state = state,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        val data: SearchData = let {
            val hospital by viewModel.hospital.collectAsStateWithLifecycle()
            val currentUserLocation by viewModel.currentUserLocation.collectAsStateWithLifecycle()

            SearchData(
                hospital = hospital,
                currentUserLocation = currentUserLocation
            )
        }

        SearchScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}