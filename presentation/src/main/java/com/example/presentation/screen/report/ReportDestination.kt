package com.example.presentation.screen.report

import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.domain.model.feature.diagnosis.AiDiagnosis
import com.example.presentation.screen.diagnosis.DiagnosisViewModel
import com.example.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.reportDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.Report.route
    ) { backStackEntry ->

        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(ScreenDestinations.Diagnosis.route)
        }

        val diagnosisViewModel: DiagnosisViewModel = hiltViewModel(parentEntry)
        val reportViewModel: ReportViewModel = hiltViewModel()

        val argument = ReportArgument(
            state = reportViewModel.state.collectAsStateWithLifecycle().value,
            screenState = reportViewModel.screenState.collectAsStateWithLifecycle().value,
            reportIntent = reportViewModel::onIntent,
            diagnosisIntent = diagnosisViewModel::onIntent,
            event = reportViewModel.eventFlow
        )

        val data: ReportData = let {
            val aiDiagnosis by diagnosisViewModel.aiDiagnosis.collectAsStateWithLifecycle()
            val userLocation by diagnosisViewModel.userLocation.collectAsStateWithLifecycle()
            val matchedHospitals by diagnosisViewModel.matchedHospitals.collectAsStateWithLifecycle()

            ReportData(
                aiDiagnosis = aiDiagnosis ?: AiDiagnosis.empty(),
                userLocation = userLocation,
                matchedHospitals = matchedHospitals
            )
        }

        ReportScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}