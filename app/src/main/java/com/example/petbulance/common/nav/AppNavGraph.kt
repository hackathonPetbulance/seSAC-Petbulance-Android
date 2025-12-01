package com.example.petbulance.common.nav

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.presentation.screen.diagnosis.diagnosisDestination
import com.example.presentation.screen.home.homeDestination
import com.example.presentation.screen.hospital.hospitalDestination
import com.example.presentation.screen.report.reportDestination
import com.example.presentation.screen.search.searchDestination
import com.example.presentation.utils.nav.ScreenDestinations

@RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
@Composable
fun AppNavGraph(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ScreenDestinations.Home.route,
        modifier = modifier
    ) {
        homeDestination(navController = navController)
        diagnosisDestination(navController = navController)
        reportDestination(navController = navController)
        hospitalDestination(navController = navController)
        searchDestination(navController = navController)
    }
}