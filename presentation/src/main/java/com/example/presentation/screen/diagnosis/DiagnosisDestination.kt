package com.example.presentation.screen.diagnosis

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.presentation.utils.nav.ScreenDestinations

@RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
fun NavGraphBuilder.diagnosisDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.Diagnosis.route
    ) {
        val viewModel: DiagnosisViewModel = hiltViewModel()

        val argument = DiagnosisArgument(
            state = viewModel.state.collectAsStateWithLifecycle().value,
            screenState = viewModel.screenState.collectAsStateWithLifecycle().value,
            intent = viewModel::onIntent,
            event = viewModel.eventFlow
        )

        val data = let {
            val imageUris by viewModel.imageUris.collectAsStateWithLifecycle()
            val animalSpecies by viewModel.animalSpecies.collectAsStateWithLifecycle()
            val description by viewModel.description.collectAsStateWithLifecycle()

            DiagnosisData(
                imageUris = imageUris,
                animalSpecies = animalSpecies,
                description = description
            )
        }

        DiagnosisScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}