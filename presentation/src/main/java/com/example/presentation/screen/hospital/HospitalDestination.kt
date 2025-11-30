package com.example.presentation.screen.hospital

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.domain.model.feature.hospitals.HospitalDetail
import com.example.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.hospitalDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.Hospital.route,
        arguments = listOf(
            navArgument(name = "hospitalId") {
                type = NavType.LongType
                defaultValue = 0L
            }
        )
    ) {
        val viewModel: HospitalViewModel = hiltViewModel()

        val argument: HospitalArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            HospitalArgument(
                state = state,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        val data: HospitalData = let {
            val hospital by viewModel.hospital.collectAsStateWithLifecycle()
            val currentUserLocation by viewModel.currentUserLocation.collectAsStateWithLifecycle()
            val reviews by viewModel.reviews.collectAsStateWithLifecycle()

            HospitalData(
                hospital = hospital ?: HospitalDetail.stub,
                currentUserLocation = currentUserLocation,
                reviews = reviews
            )
        }

        HospitalScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}